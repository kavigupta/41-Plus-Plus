package fortytwo.compiler.language.statements;

import java.util.List;

import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.identifier.FunctionName;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.expressions.Expression;

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
		// TODO do this once functions are properly implemented
		return null;
	}
	@Override
	public SentenceType type() {
		return SentenceType.FUNCTION_CALL;
	}
}
