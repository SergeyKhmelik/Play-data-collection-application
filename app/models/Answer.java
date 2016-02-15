package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.Strings;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "answers")
public class Answer {

	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "answers_seq_gen")
	@SequenceGenerator(name = "answers_seq_gen", sequenceName = "answers_seq_gen", allocationSize = 1)
	@Column(name = "id_answer")
	public int idAnswer;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_response", nullable = false)
	public Response response;

	@ManyToOne
	@JoinColumn(name = "id_field", nullable = false)
	public Field field;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@Column(nullable = true)
	public String value;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(name = "answer_option",
				joinColumns = { @JoinColumn(name = "id_answer", nullable = false) },
				inverseJoinColumns = { @JoinColumn(name = "id_option", nullable = false) })
	public Set<Option> options;

	public Answer(){}

	public Answer(Field field, Response response, Map<String, String> data) {
		this.field = field;
		this.response = response;
		setAnswerValue(this, data);
	}

	@JsonIgnore
	public boolean isEmptyAnswer() {
		return Strings.isNullOrEmpty(value) &&
				(options == null || options.isEmpty());
	}

	/**
	 * Sets answer value depending on answered field type.
	 *
	 * @param answer Answer object, where the value is set.
	 * @param data Map of request data.
	 */
	private void setAnswerValue(Answer answer, Map<String, String> data) {
		if(field.fieldType.isMultiOptional()){
			answer.options = new HashSet<>();
			if(field.fieldType.isMultiChoisable()) {
				answer.options = getOptionsFromDataMap(answer.field, data);
			} else if (!Strings.isNullOrEmpty(data.get(answer.field.label))) {
				answer.options.add(new Option(data.get(answer.field.label), answer.field));
			}
		} else {
			answer.value = data.get(answer.field.label);
		}
	}

	/**
	 * Retrieves option set from request data map.
	 *
	 * Answer options are saved in field_label[option_index] format.
	 *
	 * @param field Answering field
	 * @param data Map from request
	 * @return Set of Option objects.
	 */
	private Set<Option> getOptionsFromDataMap(Field field, Map<String, String> data) {
		Set<Option> options = new HashSet<>();
		for (int i = 0; data.get(field.label + '[' + i + ']') != null; i++) {
			Option option = new Option(data.get(field.label + '[' + i + ']'), field);
			options.add(option);
		}
		return options;
	}

	@Override
	public String toString() {
		return "Answer{" +
				"idAnswer=" + idAnswer +
				", field=" + field.label +
				", value='" + value + '\'' +
				", options=" + options +
				'}';
	}
}