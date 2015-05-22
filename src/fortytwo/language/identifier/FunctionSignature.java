package fortytwo.language.identifier;

import java.util.List;

import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.GenericType;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.expressions.Expression;

public class FunctionSignature {
	public final FunctionName name;
	public final List<GenericType> inputTypes;
	public final GenericType outputType;
	public static FunctionSignature getInstance(FunctionName name,
			List<GenericType> inputTypes, GenericType outputType) {
		return new FunctionSignature(name, inputTypes, outputType);
	}
	private FunctionSignature(FunctionName name, List<GenericType> inputTypes,
			GenericType outputType) {
		this.name = name;
		this.inputTypes = inputTypes;
		this.outputType = outputType;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((inputTypes == null) ? 0 : inputTypes.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((outputType == null) ? 0 : outputType.hashCode());
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
		if (outputType == null) {
			if (other.outputType != null) return false;
		} else if (!outputType.equals(other.outputType)) return false;
		return true;
	}
	@Override
	public String toString() {
		return "FunctionSignature [name=" + name + ", inputTypes="
				+ inputTypes + ", outputType=" + outputType + "]";
	}
	public final TypeVariableRoster typeVariables(
			List<? extends Expression> arguments) {
		TypeVariableRoster roster = new TypeVariableRoster();
		for (int i = 0; i < this.inputTypes.size(); i++) {
			ConcreteType arg = arguments.get(i).resolveType();
			GenericType expected = this.inputTypes.get(i);
			switch (expected.kind()) {
				case CONCRETE:
					if (!expected.equals(arg)) throw new RuntimeException(
					/* LOWPRI-E */);
					break;
				case CONSTRUCTOR:
				case VARIABLE:
					roster.pairs.putAll(expected.match(arg).pairs);
					break;
			}
		}
		return roster;
	}
	public boolean matches(FunctionName name, List<ConcreteType> inputs) {
		if (!this.name.equals(name)) return false;
		if (this.inputTypes.size() != inputs.size()) return false;
		return accepts(inputs);
	}
	public boolean accepts(List<ConcreteType> inputs) {
		if (inputs.size() != inputTypes.size()) return false;
		try {
			for (int i = 0; i < inputs.size(); i++)
				inputTypes.get(i).match(inputs.get(i));
			return true;
		} catch (Throwable t) {
			return false;
		}
	}
}
