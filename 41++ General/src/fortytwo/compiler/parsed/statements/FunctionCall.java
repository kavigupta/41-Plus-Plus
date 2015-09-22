package fortytwo.compiler.parsed.statements;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.language.Operation;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.FunctionType;
import fortytwo.library.standard.StdLib42;
import fortytwo.vm.environment.OrderedEnvironment;
import fortytwo.vm.environment.TypeEnvironment;
import fortytwo.vm.errors.DNEErrors;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralFunction;
import fortytwo.vm.expressions.LiteralNumber;

/**
 * Represents a function call expression, which contains a function signature
 * lookup along with the expressions that will be plugged in
 */
public class FunctionCall extends Expression {
	private final FunctionName name;
	private final List<Expression> arguments;
	public static FunctionCall getInstance(FunctionName signature,
			List<Expression> value) {
		return new FunctionCall(signature, value);
	}
	public static FunctionCall getOperation(Expression first, Operation op,
			Expression second) {
		return getInstance(
				FunctionName.getInstance(StdLib42.opName(op.display)),
				Arrays.asList(first, second));
	}
	public static Expression getNegation(Expression next) {
		return getOperation(
				new LiteralNumber(BigDecimal.ZERO, Context.SYNTHETIC),
				Operation.SUBTRACT, next);
	}
	private FunctionCall(FunctionName signature, List<Expression> arguments) {
		super(signature.context());
		this.name = signature;
		this.arguments = arguments;
	}
	@Override
	public LiteralExpression literalValue(OrderedEnvironment env) {
		final TypeEnvironment se = env.staticEnvironment();
		final List<ConcreteType> types = arguments.stream()
				.map(x -> x.type(env.staticEnvironment()))
				.collect(Collectors.toList());
		// call get because it should have been checked already
		final FunctionType sig = se.referenceTo(name, types).get();
		final Optional<LiteralFunction> f = env.container.funcs
				.get(new FunctionSignature(name, sig), types);
		if (!f.isPresent()) {
			System.out.println(name.identifier().unmangledName());
			env.container.funcs.functions.entrySet().stream().forEach(
					x -> System.out.println("\t" + x.getKey().unmangledName()));
			// crash. There is no reason this point should have been reached.
			throw new RuntimeException();
		}
		return f.get().apply(env.container, arguments.stream()
				.map(x -> x.literalValue(env)).collect(Collectors.toList()));
	}
	@Override
	public ConcreteType findType(TypeEnvironment env) {
		final List<ConcreteType> types = arguments.stream()
				.map(x -> x.type(env)).collect(Collectors.toList());
		final Optional<FunctionType> sigOpt = env.referenceTo(name, types);
		if (!sigOpt.isPresent()) DNEErrors.functionSignatureDNE(name, types);
		return sigOpt.get().outputType
				.resolve(sigOpt.get().typeVariables(arguments, env));
	}
	@Override
	public boolean typeCheck(TypeEnvironment environment) {
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
		final FunctionCall other = (FunctionCall) obj;
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
	public Optional<Expression> getSingleExpression() {
		if (name.function.size() == 1 && name.function.get(0).isArgument())
			return Optional.of(arguments.get(0));
		return Optional.empty();
	}
}
