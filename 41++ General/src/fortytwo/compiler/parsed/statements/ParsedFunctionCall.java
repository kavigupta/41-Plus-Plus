package fortytwo.compiler.parsed.statements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.expressions.LiteralExpression;

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
	public void execute(LocalEnvironment environment) {
		literalValue(environment);
	}
	@Override
	public LiteralExpression literalValue(LocalEnvironment env) {
		StaticEnvironment se = env.staticEnvironment();
		List<ConcreteType> types = arguments.stream()
				.map(x -> x.resolveType(env.staticEnvironment()))
				.collect(Collectors.toList());
		FunctionSignature sig = se.referenceTo(name, types);
		Function42 f = env.global.funcs.get(sig, arguments, types);
		if (f == null) throw new RuntimeException(sig.name.toString());
		return f.apply(
				env.global,
				arguments.stream().map(x -> x.literalValue(env))
						.collect(Collectors.toList()));
	}
	@Override
	public boolean typeCheck(StaticEnvironment env) {
		// this has already been checked.
		return true;
	}
	@Override
	public ConcreteType resolveType(StaticEnvironment env) {
		List<ConcreteType> types = arguments.stream()
				.map(x -> x.resolveType(env)).collect(Collectors.toList());
		FunctionSignature sig = env.referenceTo(name, types);
		return sig.outputType.resolve(sig.typeVariables(arguments, env));
	}
	@Override
	public SentenceType type() {
		return SentenceType.FUNCTION_CALL;
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this.name, this.arguments);
	}
	@Override
	public Context context() {
		ArrayList<Context> contexts = new ArrayList<>();
		contexts.add(name.context());
		arguments.forEach(x -> contexts.add(x.context()));
		return Context.sum(contexts);
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
