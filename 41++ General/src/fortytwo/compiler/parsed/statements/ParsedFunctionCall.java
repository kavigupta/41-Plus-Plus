package fortytwo.compiler.parsed.statements;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.FunctionType;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.errors.DNEErrors;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralFunction;

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
		// call get because it should have been checked already
		final FunctionType sig = se.referenceTo(name, types).get();
		final Optional<LiteralFunction> f = env.global.funcs
				.get(new FunctionSignature(name, sig), arguments, types);
		if (!f.isPresent())
			// crash. There is no reason this point should have been reached.
			throw new RuntimeException(name.toString());
		return f.get().apply(env.global, arguments.stream()
				.map(x -> x.literalValue(env)).collect(Collectors.toList()));
	}
	@Override
	public ConcreteType findType(StaticEnvironment env) {
		final List<ConcreteType> types = arguments.stream()
				.map(x -> x.type(env)).collect(Collectors.toList());
		final Optional<FunctionType> sigOpt = env.referenceTo(name, types);
		if (!sigOpt.isPresent()) DNEErrors.functionSignatureDNE(name, types);
		return sigOpt.get().outputType
				.resolve(sigOpt.get().typeVariables(arguments, env));
	}
	@Override
	public boolean typeCheck(StaticEnvironment environment) {
		findType(environment);
		return true;
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
	public Optional<VariableIdentifier> identifier() {
		return Optional.empty();
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
