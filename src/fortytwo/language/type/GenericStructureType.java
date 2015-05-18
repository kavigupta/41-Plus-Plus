package fortytwo.language.type;

import java.util.List;

public class GenericStructureType implements GenericType {
	public final List<String> name;
	public final List<TypeVariable> typeVariables;
	public GenericStructureType(List<String> name,
			List<TypeVariable> typeVariables) {
		this.name = name;
		this.typeVariables = typeVariables;
	}
	@Override
	public Kind kind() {
		return Kind.CONSTRUCTOR;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		GenericStructureType other = (GenericStructureType) obj;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		if (typeVariables == null) {
			if (other.typeVariables != null) return false;
		} else if (!typeVariables.equals(other.typeVariables)) return false;
		return true;
	}
}
