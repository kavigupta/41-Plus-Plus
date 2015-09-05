package fortytwo.compiler.parsed.statements;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.expressions.LiteralExpression;

public class ParsedFunctionCall extends Expression {
	public final FunctionName name;
	public final List<Expression> arguments;
	public static ParsedFunctionCall getInstance(FunctionName signature,
			List<Expression> value) {
		return new ParsedFunctionCall(signature, value);
	}
	private ParsedFunctionCall(FunctionName signature,
			List<Expression> arguments) {
		super(signature.context());
		this.name = signature;
		this.arguments = arguments;
	}
	@Override
	public LiteralExpression literalValue(LocalEnvironment env) {
		final StaticEnvironment se = env.staticEnvironment();
		final List<ConcreteType> types = arguments.stream()
				.map(x -> x.type(env.staticEnvironment()))
				.collect(Collectors.toList());
		final FunctionSignature sig = se.referenceTo(name, types);
		final Optional<Function42> f = env.global.funcs.get(sig, arguments,
				types);
		if (!f.isPresent()) throw new RuntimeException(sig.name.toString());
		return f.get().apply(env.global, arguments.stream()
				.map(x -> x.literalValue(env)).collect(Collectors.toList()));
	}
	@Override
	public ConcreteType findType(StaticEnvironment env) {
		final List<ConcreteType> types = arguments.stream()
				.map(x -> x.type(env)).collect(Collectors.toList());
		final FunctionSignature sig = env.referenceTo(name, types);
		return sig.outputType.resolve(sig.typeVariables(arguments, env));
	}
	@Override
	public SentenceType kind() {
		return SentenceType.FUNCTION_CALL;
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this.name, this.arguments);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (arguments == null ? 0 : arguments.hashCode());
		result = prime * result + (name == null ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final ParsedFunctionCall other = (ParsedFunctionCall) obj;
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
