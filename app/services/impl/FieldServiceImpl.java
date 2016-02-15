package services.impl;

import java.util.List;
import java.util.Objects;

import com.mysema.query.jpa.impl.JPADeleteClause;
import models.Field;
import models.QAnswer;
import models.QField;

import models.QResponse;
import org.apache.commons.collections.CollectionUtils;

import com.mysema.query.jpa.impl.JPAQuery;

import play.db.jpa.JPA;


import services.FieldService;
import services.OptionMerging;

public class FieldServiceImpl implements FieldService, OptionMerging {

	public static final int NEW_FIELD_ID = 0;

	@Override
	public Field saveField(Field field) {
		if(field.idField == NEW_FIELD_ID && exists(field.label)) {
			return null;
		}
		JPA.em().merge(field);
		return field;
	}

	@Override
	public Field updateField(Field field) {
		if(hasAnswers(field) && hasImmutablePropertiesUpdate(field)) {
			return null;
		}
		field.options = mergeOptions(field.options, field);
		JPA.em().merge(field);
		return field;
	}

	@Override
	public Field getFieldById(int idField) {
		return JPA.em().find(Field.class, idField);
	}

	@Override
	public List<Field> getAllFields() {
		QField field = QField.field;
		JPAQuery query = new JPAQuery(JPA.em());
		return  query.from(field).orderBy(field.idField.asc()).list(field);
	}

	@Override
	public List<Field> getAllActiveFields() {
		QField field = QField.field;
		JPAQuery query = new JPAQuery(JPA.em());
		return query.from(field)
				.where(field.active.eq(true))
				.orderBy(field.idField.asc())
				.list(field);
	}

	@Override
	public String deleteField(int idField) {
		Field field = JPA.em().find(Field.class, idField);
		if(field == null) return null;
		JPA.em().remove(field);
		deleteEmptyResponses();
		return field.label;
	}

	/**
	 * Checks whether field with such label is already stored in DB
	 *
	 * @param fieldLabel String field label
	 * @return true - if field exists in DB, false - if field there is no field with such label
	 */
	private boolean exists(String fieldLabel) {
		QField field = QField.field;
		JPAQuery query = new JPAQuery(JPA.em());
		return query.from(field).where(field.label.eq(fieldLabel)).exists();
	}

	/**
	 * Checks whether field object has update of immutable properties
	 * such as field label, field type or field options
	 *
	 * @param field Field object
	 * @return  true - if field contains immutable properties update,
	 * 			false - if field has no immutable properties updates
	 */
	private boolean hasImmutablePropertiesUpdate(Field field) {
		Field dbField = JPA.em().find(Field.class, field.idField);
		if(field.fieldType != dbField.fieldType) return true;
		if(!Objects.equals(field.label, dbField.label)) return true;
		if(!CollectionUtils.isEqualCollection(field.options, dbField.options)) return true;

		return false;
	}

	/**
	 * Checks whether field has been answered at least once
	 *
	 * @param field Field object
	 * @return true - if field has at least one answer, false - if field has not been answered yet
	 */
	private boolean hasAnswers(Field field) {
		QAnswer answer = QAnswer.answer;
		JPAQuery query = new JPAQuery(JPA.em());
		return query.from(answer).where(answer.field.eq(field)).exists();
	}

	/**
	 * Removes all fields that have no answers.
	 */
	private void deleteEmptyResponses() {
		QResponse response = QResponse.response;
		JPADeleteClause deleteClause = new JPADeleteClause(JPA.em(), response);
		deleteClause.where(response.answers.size().eq(0)).execute();
	}
}