package services;

import java.util.List;

import com.google.inject.ImplementedBy;
import models.Field;
import services.impl.FieldServiceImpl;

/**
 * Field service interface that declares main Field business logic methods.
 * Field business logic contains operations with Option object, therefore
 * this interface extends OptionMerging interface.
 */
@ImplementedBy(FieldServiceImpl.class)
public interface FieldService extends OptionMerging{

	/**
	 * Saves new field object into DB
	 *
	 * @param field Field object
	 * @return saved field object
	 */
	Field saveField(Field field);

	/**
	 * Updates field object in DB
	 * If field has been already answered and updated Field object contains
	 * updates for field options, field type or name - returns null
	 *
	 * @param field Field object that is used to merge updates
	 * @return updated field object or null if updating field object has immutable field updates
	 */
	Field updateField(Field field);

	/**
	 * Returns field object from DB, selected by field identifier
	 *
	 * @param idField field identifier
	 * @return Field object
	 */
	Field getFieldById(int idField);

	/**
	 * Returns List of all fields, stored in DB.
	 *
	 * @return List of Field objects
	 */
	List<Field> getAllFields();

	/**
	 * Returns List of active fields, stored in DB.
	 *
	 * @return List of Field objects, which have "active" parameter set to true.
	 */
	List<Field> getAllActiveFields();

	/**
	 * Deletes field by its identifier.

	 * @param idField field identifier
	 * @return  String deleted field label if field was successfully deleted or
	 * 			null if field with such identifier is absent in DB
	 */
	String deleteField(int idField);
}