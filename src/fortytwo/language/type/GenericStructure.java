package fortytwo.language.type;

import java.util.ArrayList;
import java.util.List;

import fortytwo.language.field.GenericField;

public class GenericStructure implements GenericType {
	public final List<String> name;
	public final List<GenericType> typeVariables;
	public final List<GenericField> fields;
	public GenericStructure(List<String> name,
			List<GenericType> typeVariables, ArrayList<GenericField> fields) {
		this.name = name;
		this.typeVariables = typeVariables;
		this.fields = fields;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fields == null) ? 0 : fields.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((typeVariables == null) ? 0 : typeVariables.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		GenericStructure other = (GenericStructure) obj;
		if (fields == null) {
			if (other.fields != null) return false;
		} else if (!fields.equals(other.fields)) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		if (typeVariables == null) {
			if (other.typeVariables != null) return false;
		} else if (!typeVariables.equals(other.typeVariables)) return false;
		return true;
	}
}
