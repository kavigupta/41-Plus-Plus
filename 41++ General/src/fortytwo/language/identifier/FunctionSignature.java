package fortytwo.language.identifier;

import java.util.List;

import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.GenericType;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.environment.TypeVariableRoster;

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
	public final TypeVariableRoster typeVariables(
			List<? extends Expression> arguments, StaticEnvironment env) {
		final TypeVariableRoster roster = new TypeVariableRoster();
		for (int i = 0; i < this.inputTypes.size(); i++) {
			final ConcreteType arg = arguments.get(i).type(env);
			final GenericType expected = this.inputTypes.get(i);
			switch (expected.kind()) {
				case CONCRETE:
					// this should be the same because of prior checking.
					break;
				case CONSTRUCTOR:
				case VARIABLE:
					// does not check the other condition since variable match
					// should always succeed
					roster.pairs.putAll(expected.match(arg).get().pairs);
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
		for (int i = 0; i < inputs.size(); i++)
			if (!inputTypes.get(i).match(inputs.get(i)).isPresent())
				return false;;
		return true;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (inputTypes == null ? 0 : inputTypes.hashCode());
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result
				+ (outputType == null ? 0 : outputType.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final FunctionSignature other = (FunctionSignature) obj;
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
		return "FunctionSignature [name=" + name + ", inputTypes=" + inputTypes
				+ ", outputType=" + outputType + "]";
	}
}
