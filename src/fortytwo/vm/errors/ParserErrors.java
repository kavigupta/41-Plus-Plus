package fortytwo.vm.errors;

import java.util.List;

import fortytwo.compiler.Token;
import fortytwo.compiler.parsed.declaration.FunctionDefinition;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.compiler.parsed.sentences.Sentence;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.GenericType;

public class ParserErrors {
	public static void expectedVariableInDecl(boolean functionDecl,
			Token problem, List<Token> line) {
		Errors.error(
				ErrorType.PARSING,
				String.format(
						"A %s definition must contain only name tokens and variables, but ~%s~ is neither.",
						functionDecl ? "function" : "type", problem.token),
				problem.context);
	}
	/**
	 * Use -1 for {@code argument} to signify output type. The rest signify
	 * input type.
	 */
	public static void expectedCTInFunctionDecl(GenericType type,
			List<Token> line, int argument) {
		// TODO Auto-generated method stub
	}
	public static void expectedCTInDefinition(GenericType type) {
		// TODO Auto-generated method stub
	}
	public static void incompleteFields(VariableIdentifier vid,
			List<Token> line) {
		// TODO Auto-generated method stub
	}
	public static void noExit(FunctionDefinition f) {
		// TODO Auto-generated method stub
	}
	public static void noCloseVB(List<Token> openingBrace) {
		// TODO Auto-generated method stub
	}
	public static void expectedFunctionCall(ParsedExpression exp) {
		// TODO Auto-generated method stub
	}
	public static void expectedStatement(Sentence x) {
		// TODO Auto-generated method stub
	}
	public static void expectedDeclarationOrDefinition(Sentence s) {
		// TODO Auto-generated method stub
	}
	public static void expectedImpureExpression(List<Token> line) {
		// TODO Auto-generated method stub
	}
	public static void expectedVariableInFieldAccess(
			ParsedExpression parsedExpression) {
		// TODO Auto-generated method stub
	}
}
