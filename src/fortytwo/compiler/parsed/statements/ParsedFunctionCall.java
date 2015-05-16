package fortytwo.compiler.parsed.statements;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.statements.FunctionCall;

public class ParsedFunctionCall implements ParsedExpression, ParsedStatement {
	public final FunctionName signature;
	public final List<ParsedExpression> arguments;
	public static ParsedFunctionCall getInstance(FunctionName signature,
			List<ParsedExpression> value) {
		return new ParsedFunctionCall(signature, value);
	}
	private ParsedFunctionCall(FunctionName signature,
			List<ParsedExpression> arguments) {
		this.signature = signature;
		this.arguments = arguments;
	}
	@Override
	public Expression contextualize(LocalEnvironment env) {
		Function<ParsedExpression, ConcreteType> typeResolver = x -> x
				.contextualize(env).literalValue(env).resolveType();
		Function42 function = env.global.funcs.get(FunctionSignature
				.getInstance(signature, arguments.stream()
						.map(typeResolver).collect(Collectors.toList())));
		return FunctionCall.getInstance(
				function,
				arguments.stream().map(x -> x.contextualize(env))
						.collect(Collectors.toList()));
	}
	@Override
	public SentenceType type() {
		return SentenceType.FUNCTION_CALL;
	}
}
