package fortytwo.compiler.language.statements;

import java.util.List;

import lib.standard.collections.Pair;
import fortytwo.compiler.language.declaration.FunctionSignature;
import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.expressions.Expression;

public class ParsedFunctionCall implements ParsedExpression, ParsedStatement {
	public final FunctionSignature signature;
	public final List<Pair<VariableIdentifier, ParsedExpression>> arguments;
	public static ParsedFunctionCall getInstance(FunctionSignature signature,
			List<Pair<VariableIdentifier, ParsedExpression>> arguments) {
		return new ParsedFunctionCall(signature, arguments);
	}
	private ParsedFunctionCall(FunctionSignature signature,
			List<Pair<VariableIdentifier, ParsedExpression>> arguments) {
		this.signature = signature;
		this.arguments = arguments;
	}
	@Override
	public Expression contextualize(LocalEnvironment env) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public SentenceType type() {
		// TODO Auto-generated method stub
		return null;
	}
}
