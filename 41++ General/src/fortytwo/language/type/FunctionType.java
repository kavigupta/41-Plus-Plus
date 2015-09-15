package fortytwo.language.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.language.Language;
import fortytwo.language.SourceCode;
import fortytwo.vm.environment.TypeEnvironment;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.expressions.LiteralExpression;

public class FunctionType implements ConcreteType {
	public final List<GenericType> inputTypes;
	public final GenericType outputType;
	public static FunctionType getInstance(GenericType... types) {
		List<GenericType> inputs = new ArrayList<>();
		for (int i = 0; i < types.length - 1; i++) {
			inputs.add(types[i]);
		}
		return new FunctionType(inputs, types[types.length - 1]);
	}
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
		if (this.inputTypes.size() == 0 && this.outputType.isVoid())
			return "procedure";
		List<String> inputs = inputTypes.stream().map(GenericType::toSourceCode)
				.map(Language::articleized).collect(Collectors.toList());
		StringBuilder sb = new StringBuilder();
		sb.append("(function that ");
		if (inputs.size() != 0)
			sb.append("takes ").append(SourceCode.displayList(inputs));
		if (inputs.size() != 0 && !outputType.isVoid()) sb.append(" and ");
		if (!outputType.isVoid()) sb.append("outputs ")
				.append(Language.articleized(outputType.toSourceCode()));
		return sb.append(')').toString();
	}
	@Override
	public LiteralExpression defaultValue() {
		// TODO complete when functions are properly first-class
		return null;
	}
	public boolean accepts(List<ConcreteType> inputs) {
		if (inputs.size() != inputTypes.size()) return false;
		for (int i = 0; i < inputs.size(); i++)
			if (!inputTypes.get(i).match(inputs.get(i)).isPresent())
				return false;;
		return true;
	}
	public final TypeVariableRoster typeVariables(
			List<? extends Expression> arguments, TypeEnvironment env) {
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
