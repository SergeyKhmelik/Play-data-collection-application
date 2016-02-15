package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;

import play.Logger;
import play.data.validation.ValidationError;

import java.time.LocalDateTime;
import java.util.*;

import javax.persistence.*;

@Entity
@Table(name = "responses")
public class Response {

    public static final String DATE_REGEX = "[0-9]{4}-[0-9]{2}-[0-9]{2}";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "responses_seq_gen")
    @SequenceGenerator(name = "responses_seq_gen", sequenceName = "responses_seq_gen", allocationSize = 1)
    @Column(name = "id_resoponse")
    public int idResponse;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "response", fetch = FetchType.EAGER)
    public Set<Answer> answers;

    @JsonIgnore
    @Transient
    public LocalDateTime time;

    public Response() {
    }

    public Response(Map<String, String> data, List<Field> activeFields) {
        this.time = LocalDateTime.now();
        this.answers = new HashSet<>();

        for (Field field : activeFields) {
            Answer answer = new Answer(field, this, data);
            this.answers.add(answer);
        }
    }

    /**
     * Validates Response object.
     *
     * Returns List of validation errors if the object is not valid.
     * Main rules: if field is required, response answer on that field should not be empty,
     * if field type is date, response answer on that field should match DATE_REGEX.
     *
     * @return List of validation errors or null if Response object is valid
     */
    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();
        if(answers != null) {
            for (Answer answer : answers) {
                if (answer.field.required && answer.isEmptyAnswer()) {
                	errors.add(new ValidationError(answer.field.label, "Field is required."));
                }
                if(answer.field.fieldType.equals(FieldType.DATE) && !isValidDate(answer.value)) {
                    errors.add(new ValidationError(answer.field.label,
                            "Wrong date format. Please, use yyyy-mm-dd format."));
                }
            }
        }
        return errors.isEmpty() ? null : errors;
    }

    /**
     * Validates date format string/
     *
     * @param value date string
     * @return  true - if string is valid date,
     *          false - if string is not valid date
     */
    private boolean isValidDate(String value) {
        return value.matches(DATE_REGEX);
    }

    @Override
    public String toString() {
        return "Response{" +
                "idResponse=" + idResponse +
                ", answers=" + answers +
                '}';
    }
}