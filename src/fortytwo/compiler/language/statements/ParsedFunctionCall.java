package fortytwo.compiler.language.statements;

import java.util.List;

import lib.standard.collections.Pair;
import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.expressions.ParsedVariable;
import fortytwo.compiler.language.statements.constructions.FunctionSignature;
import fortytwo.vm.environment.Environment;
import fortytwo.vm.expressions.Expression;

public class ParsedFunctionCall implements ParsedExpression {
	public final FunctionSignature signature;
	public final List<Pair<ParsedVariable, ParsedExpression>> arguments;
	public static ParsedFunctionCall getInstance(FunctionSignature signature,
			List<Pair<ParsedVariable, ParsedExpression>> arguments) {
		return new ParsedFunctionCall(signature, arguments);
	}
	private ParsedFunctionCall(FunctionSignature signature,
			List<Pair<ParsedVariable, ParsedExpression>> arguments) {
		this.signature = signature;
		this.arguments = arguments;
	}
	@Override
	public Expression contextualize(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
}
