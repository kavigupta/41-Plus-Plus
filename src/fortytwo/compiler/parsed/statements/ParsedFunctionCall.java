package fortytwo.compiler.parsed.statements;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.language.SourceCode;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.statements.FunctionCall;

public class ParsedFunctionCall implements ParsedExpression, ParsedStatement {
	public final FunctionName name;
	public final List<ParsedExpression> arguments;
	public static ParsedFunctionCall getInstance(FunctionName signature,
			List<ParsedExpression> value) {
		return new ParsedFunctionCall(signature, value);
	}
	private ParsedFunctionCall(FunctionName signature,
			List<ParsedExpression> arguments) {
		this.name = signature;
		this.arguments = arguments;
	}
	@Override
	public Expression contextualize(StaticEnvironment env) {
		Function<ParsedExpression, ConcreteType> typeResolver = x -> x
				.contextualize(env).resolveType();
		List<ConcreteType> types = arguments.stream().map(typeResolver)
				.collect(Collectors.toList());
		System.out.println(arguments);
		System.out.println(types);
		FunctionSignature sig = env.funcs.referenceTo(name, types);
		return FunctionCall.getInstance(
				sig,
				sig.outputType.resolve(sig.typeVariables(arguments.stream()
						.map(x -> x.contextualize(env))
						.collect(Collectors.toList()))),
				arguments.stream().map(x -> x.contextualize(env))
						.collect(Collectors.toList()));
	}
	@Override
	public void decontextualize(StaticEnvironment env) {
		arguments.stream().forEach(x -> x.decontextualize(env));
	}
	@Override
	public SentenceType type() {
		return SentenceType.FUNCTION_CALL;
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((arguments == null) ? 0 : arguments.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ParsedFunctionCall other = (ParsedFunctionCall) obj;
		if (arguments == null) {
			if (other.arguments != null) return false;
		} else if (!arguments.equals(other.arguments)) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		return true;
	}
	@Override
	public String toString() {
		return toSourceCode();
	}
}
