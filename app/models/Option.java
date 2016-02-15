package models;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "options", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"id_field","value"})})
public class Option {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "options_seq_gen")
	@SequenceGenerator(name = "options_seq_gen", sequenceName = "options_seq_gen", allocationSize = 1)
	@Column(name = "id_option")
	public int idOption;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_field", nullable = false)
	public Field field;

	@Column(nullable = false)
	public String value;

	public Option() {}

	public Option(String optionString, Field field) {
		this.value = optionString;
		this.field = field;
	}

	@Override
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o, "idOption");
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, "idOption");
	}

	@Override
	public String toString() {
		return value;
	}

	@JsonValue
	public String toJson(){
		return value;
	}
}