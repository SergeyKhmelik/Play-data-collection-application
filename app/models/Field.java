package models;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonValue;
import models.dto.FieldDTO;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "fields")
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fields_seq_gen")
    @SequenceGenerator(name = "fields_seq_gen", sequenceName = "fields_seq_gen", allocationSize = 1)
    @Column(name = "id_field")
    public int idField;

    @Column(unique = true, nullable = false)
    public String label;

    @Enumerated(value = EnumType.STRING)
    @Column(updatable = false)
    public FieldType fieldType;

    @Column(nullable = false)
    public boolean required;

    @Column(nullable = false)
    public boolean active;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "field", fetch = FetchType.EAGER, orphanRemoval = true)
    @Fetch(FetchMode.SELECT)
    public Set<Option> options;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "field")
    public Set<Answer> answers;

    public Field() {
    }

    public Field(FieldDTO fieldDTO) {
        idField = fieldDTO.idField;
        label = fieldDTO.label;
        active = fieldDTO.active;
        required = fieldDTO.required;
        fieldType = FieldType.getTypeByUserRepresentation(fieldDTO.fieldType);
        if (fieldDTO.options != null && !fieldDTO.options.isEmpty()) {
            options = fieldDTO.options.stream()
                    .distinct()
                    .map(optionString -> new Option(optionString, this))
                    .collect(Collectors.toSet());
        } else {
            options = new HashSet<>();
        }
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o, "options", "answers");
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "options", "answers");
    }

    @JsonValue
    public String toJson() {
        return label;
    }

    @Override
    public String toString() {
        return "Field{" +
                "idField=" + idField +
                ", label='" + label + '\'' +
                ", fieldType=" + fieldType +
                ", required=" + required +
                ", active=" + active +
                ", options=" + options +
                '}';
    }
}