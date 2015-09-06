package fortytwo.language.type;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import fortytwo.compiler.Context;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.expressions.LiteralExpression;

public class FunctionType implements ConcreteType {
	private List<GenericType> inputTypes;
	private GenericType outputType;
	public FunctionType(List<GenericType> inputTypes, GenericType outputType) {
		super();
		this.inputTypes = inputTypes;
		this.outputType = outputType;
	}
	@Override
	public Kind kind() {
		return Kind.CONCRETE;
	}
	@Override
	public Optional<TypeVariableRoster> match(ConcreteType toMatch) {
		if (this.equals(toMatch)) return Optional.of(new TypeVariableRoster());
		return Optional.empty();
	}
	@Override
	public ConcreteType resolve(TypeVariableRoster roster) {
		return this;
	}
	@Override
	public Context context() {
		return Context.sum(Arrays.asList(Context.sum(inputTypes), outputType));
	}
	@Override
	public String toSourceCode() {
		Optional<String> inputs = inputTypes.stream()
				.map(GenericType::toSourceCode).reduce((a, b) -> a + "," + b);
		String renderedInputs = inputs.isPresent() ? inputs.get() : "";
		return "(" + renderedInputs + ") -> " + outputType.toSourceCode();
	}
	@Override
	public LiteralExpression defaultValue() {
		// TODO complete when functions are properly first-class
		return null;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((inputTypes == null) ? 0 : inputTypes.hashCode());
		result = prime * result
				+ ((outputType == null) ? 0 : outputType.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		FunctionType other = (FunctionType) obj;
		if (inputTypes == null) {
			if (other.inputTypes != null) return false;
		} else if (!inputTypes.equals(other.inputTypes)) return false;
		if (outputType == null) {
			if (other.outputType != null) return false;
		} else if (!outputType.equals(other.outputType)) return false;
		return true;
	}
	@Override
	public String toString() {
		return "FunctionType [inputTypes=" + inputTypes + ", outputType="
				+ outputType + "]";
	}
}
