package fortytwo.compiler.parsed.statements;

import java.util.*;
import java.util.stream.Collectors;

import fortytwo.compiler.LiteralToken;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.language.Operation;
import fortytwo.language.Resources;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.FunctionType;
import fortytwo.language.type.GenericType;
import fortytwo.library.standard.StdLib42;
import fortytwo.vm.environment.OrderedEnvironment;
import fortytwo.vm.environment.type.AbstractTypeEnvironment;
import fortytwo.vm.environment.type.AbstractTypeEnvironment.RequestType;
import fortytwo.vm.environment.type.NonTopTypeEnvironment;
import fortytwo.vm.errors.DNEErrors;
import fortytwo.vm.errors.ParserErrors;
import fortytwo.vm.errors.TypingErrors;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralFunction;

/**
 * Represents a function call expression, which contains a function signature
 * lookup along with the expressions that will be plugged into it.
 */
public class FunctionCall extends Expression {
	/**
	 * The name of the function to call
	 */
	private final FunctionName name;
	/**
	 * The inputs into the function
	 */
	private final List<Expression> arguments;
	/**
	 * Gets the function call given the function name and a list of inputs
	 * 
	 * @param name
	 *        the function name
	 * @param inputs
	 *        the inputs
	 * @return a function call object representing {@code name(inputs)}
	 */
	public static FunctionCall getInstance(FunctionName name,
			List<Expression> inputs) {
		return new FunctionCall(name, inputs);
	}
	/**
	 * Creates a function call representing an operation between two
	 * expressions.
	 * 
	 * @param first
	 *        the first expression in the operation
	 * @param op
	 *        the operation to apply
	 * @param second
	 *        the second expression in the operation
	 * @return
	 */
	public static FunctionCall getOperation(Expression first, Operation op,
			Expression second) {
		return getInstance(
				FunctionName.getInstance(StdLib42.opName(op.display)),
				Arrays.asList(first, second));
	}
	/**
	 * Gets a negation from the AST.
	 * 
	 * @param x
	 * @return {@code -x}
	 */
	public static Expression getNegation(Expression x) {
		return new FunctionCall(
				FunctionName.getInstance(Resources.SUBTRACTION_SIGN, ""),
				Arrays.asList(x));
	}
	private FunctionCall(FunctionName signature, List<Expression> arguments) {
		super(signature.context());
		this.name = signature;
		this.arguments = arguments;
	}
	public Optional<Expression> getSingleExpression() {
		if (name.function.size() == 1 && name.function.get(0).isArgument())
			return Optional.of(arguments.get(0));
		return Optional.empty();
	}
	public List<VariableIdentifier> getArgumentsAsVariables() {
		return arguments.stream().map(x -> {
			if (!x.identifier().isPresent())
				ParserErrors.expectedVariableInDecl(true, x.toToken());
			return x.identifier().get();
		}).collect(Collectors.toList());
	}
	public FunctionSignature definitionSignature(
			Map<VariableIdentifier, GenericType> varTypes,
			GenericType outputType, List<LiteralToken> line) {
		final List<VariableIdentifier> variables = getArgumentsAsVariables();
		final List<GenericType> types = new ArrayList<>();
		for (final VariableIdentifier vid : variables) {
			final GenericType gt = varTypes.get(vid);
			if (gt == null)
				TypingErrors.incompleteFieldTypingInFunctionDecl(vid, line);
			types.add(gt);
		}
		return new FunctionSignature(name, new FunctionType(types, outputType));
	}
	@Override
	public LiteralExpression literalValue(OrderedEnvironment env) {
		final NonTopTypeEnvironment se = env.staticEnvironment();
		final List<ConcreteType> types = arguments.stream()
				.map(x -> x.type(env.staticEnvironment()))
				.collect(Collectors.toList());
		// call get because it should have been checked already
		final FunctionType sig = se.referenceTo(name, types, RequestType.ANY)
				.orElseGet(() -> {
					DNEErrors.functionSignatureDNE(name, types);
					throw new IllegalArgumentException();
				});
		final Optional<LiteralFunction> f = env
				.getFunction(new FunctionSignature(name, sig), types);
		if (!f.isPresent()) {
			System.out.println(name.identifier().unmangledName());
			// crash. There is no reason this point should have been reached.
			throw new RuntimeException();
		}
		return f.get().apply(env.staticEnvironment(), arguments.stream()
				.map(x -> x.literalValue(env)).collect(Collectors.toList()));
	}
	@Override
	public ConcreteType findType(AbstractTypeEnvironment env) {
		final List<ConcreteType> types = arguments.stream()
				.map(x -> x.type(env)).collect(Collectors.toList());
		final FunctionType sigOpt = env
				.referenceTo(name, types, RequestType.ANY).orElseGet(() -> {
					DNEErrors.functionSignatureDNE(name, types);
					throw new IllegalArgumentException();
				});
		return sigOpt.outputType.resolve(sigOpt.typeVariables(arguments, env));
	}
	@Override
	public boolean typeCheck(AbstractTypeEnvironment environment) {
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
}
