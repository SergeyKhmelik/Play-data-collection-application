package models;

/**
 * Field type enum, that contains FieldTypes, their user representation form,
 * information about multi-optional status and multi-choisable status.
 */
public enum FieldType {

    SINGLE_LINE_TEXT("single line text", false, false),
    MULTI_LINE_TEXT("multi line text", false, false),
    RADIO("radio button", true, false),
    CHECKBOX("check box", true, true),
    COMBOBOX("combo box", true, false),
    DATE("date", false, false),
    RANGE("slider", false, false);

    private String userRepresentation;
    private boolean multiOptional;
    private boolean multiChoisable;

    private FieldType(String userRepresentation, boolean multiOptional, boolean multiChoisable) {
        this.userRepresentation = userRepresentation;
        this.multiOptional = multiOptional;
        this.multiChoisable = multiChoisable;
    }

    /**
     * Retrieves FieldType object by its users representation value
     *
     * @param userRepresentation String user representation value
     * @return FieldType object or null if there is no field type with such user representation.
     */
    public static FieldType getTypeByUserRepresentation(String userRepresentation) {
        for(FieldType type : FieldType.values()) {
            if(type.userRepresentation.equals(userRepresentation)) {
                return type;
            }
        }
        return null;
    }

    public String getUserRepresentation() {
        return userRepresentation;
    }

    public boolean isMultiOptional() {
        return multiOptional;
    }

    public boolean isMultiChoisable() {
        return multiChoisable;
    }
}