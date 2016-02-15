package models.dto;

import com.google.common.base.Strings;
import models.Field;
import models.FieldType;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Field data transfer object, that is used to bind field-form data.
 */
public class FieldDTO {

    private static final int MIN_OPTIONS_NUMBER = 2;

    public int idField;

    public String label;

    public String fieldType;

    public boolean required;

    public boolean active;

    public List<String> options;

    public FieldDTO() {}

    public FieldDTO(Field field) {
        idField = field.idField;
        label = field.label;
        fieldType = field.fieldType.getUserRepresentation();
        required = field.required;
        active = field.active;
        options = field.options.stream()
                .map(option -> option.value)
                .collect(Collectors.toList());
    }

    /**
     * Validates FieldDTO object.
     *
     * Returns List of validation errors if the object is not valid.
     * Main rules: field label should not be empty and multichoisable fields should contain
     * at least two options.
     *
     * @return List of validation errors or null if FieldDTO object is valid
     */
    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();
        if (Strings.isNullOrEmpty(label))
            errors.add(new ValidationError("label", "Label should not be empty."));

        if (FieldType.getTypeByUserRepresentation(fieldType).isMultiChoisable()) {
            if (options == null || options.isEmpty() || options.size() < MIN_OPTIONS_NUMBER) {
                errors.add(new ValidationError("options", "Multi-optional field should contain at least two options."));
            }
        }
        return errors.isEmpty() ? null : errors;
    }
}