package fortytwo.vm.constructions;

import java.util.List;
import java.util.Optional;

import fortytwo.language.field.TypedVariable;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.StructureType;

public class Structure {
	public final StructureType type;
	public final List<TypedVariable> fields;
	public Structure(StructureType type, List<TypedVariable> fields) {
		this.type = type;
		this.fields = fields;
	}
	public Optional<ConcreteType> typeof(VariableIdentifier fieldName) {
		for (TypedVariable field : fields) {
			if (field.name.equals(fieldName)) return Optional.of(field.type);
		}
		return Optional.empty();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (fields == null ? 0 : fields.hashCode());
		result = prime * result + (type == null ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final Structure other = (Structure) obj;
		if (fields == null) {
			if (other.fields != null) return false;
		} else if (!fields.equals(other.fields)) return false;
		if (type == null) {
			if (other.type != null) return false;
		} else if (!type.equals(other.type)) return false;
		return true;
	}
	@SuppressWarnings("boxing")
	public boolean containsField(VariableIdentifier k) {
		return fields.stream().map(x -> x.name.equals(k)).reduce(false,
				(a, b) -> a || b);
	}
	@Override
	public String toString() {
		return "Structure [type=" + type + ", fields=" + fields + "]";
	}
}
