package fortytwo.language.identifier;

import java.util.List;

import fortytwo.language.type.ConcreteType;

public class FunctionSignature {
	public final FunctionName name;
	public final List<ConcreteType> inputTypes;
	public static FunctionSignature getInstance(FunctionName name,
			List<ConcreteType> inputTypes) {
		return new FunctionSignature(name, inputTypes);
	}
	private FunctionSignature(FunctionName name, List<ConcreteType> inputTypes) {
		this.name = name;
		this.inputTypes = inputTypes;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((inputTypes == null) ? 0 : inputTypes.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		FunctionSignature other = (FunctionSignature) obj;
		if (inputTypes == null) {
			if (other.inputTypes != null) return false;
		} else if (!inputTypes.equals(other.inputTypes)) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		return true;
	}
}
