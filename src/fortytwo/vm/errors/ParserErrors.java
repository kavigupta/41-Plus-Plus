package fortytwo.vm.errors;

import java.util.ArrayList;
import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.language.Operation;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.GenericType;

public class ParserErrors {
	public static void closingBracketNotFound(Context parent, String input,
			int i) {
		// TODO 0 implement
	}
	public static void closingParenNotFound(Context parent, String input, int i) {
		// TODO 0 implement
	}
	public static void closeMarkerWithNoOpenMarker(Context parent,
			String input, int i) {
		// TODO 0 implement
	}
	public static void closingQuoteNotFound(Context parent, String input, int i) {
		// TODO 0 implement
	}
	public static void invalidStructDefinition(List<Token> line) {
		// TODO Auto-generated method stub
	}
	public static void invalidFunctionDeclaration(List<Token> line) {
		// TODO Auto-generated method stub
	}
	public static void invalidFunctionDeclSuffix(List<Token> line) {
		// TODO Auto-generated method stub
	}
	public static void nonVariableInFunctionDecl(ParsedExpression x,
			List<Token> line) {
		// TODO Auto-generated method stub
	}
	public static void incompleteFields(VariableIdentifier vid,
			List<Token> line) {
		// TODO Auto-generated method stub
	}
	public static void genericTypeInFunctionDecl(GenericType type,
			List<Token> line) {
		// TODO Auto-generated method stub
	}
	public static void genericTypeInFunctionOutput(GenericType outputType,
			List<Token> line) {
		// TODO Auto-generated method stub
	}
	public static void errorInParsingArithmetic(List<Token> currentExpression) {
		// TODO Auto-generated method stub
	}
	public static void noPriorToken(Context context, Operation operator,
			ArrayList<ParsedExpression> expressions) {
		// TODO Auto-generated method stub
	}
	public static void errorInParsingNumber(Token token) {
		// TODO Auto-generated method stub
	}
	public static void invalidOperator(Token token) {
		// TODO Auto-generated method stub
	}
	public static void expectedVariableButReceivedConcrete(Token token) {
		// TODO Auto-generated method stub
	}
	public static void expectedConcreteButReceivedVariable(Token token) {
		// TODO Auto-generated method stub
	}
	public static void constructorInTypeDecl(Token token) {
		// TODO Auto-generated method stub
	}
	public static void invalidArrayDecl(List<Token> tokens,
			List<GenericType> typeVariables) {
		// TODO Auto-generated method stub
	}
	public static void invalidVariableIdenitifer(Token token) {
		// TODO Auto-generated method stub
	}
	public static void nonVariableInFieldAccess(
			ParsedExpression parsedExpression) {
		// TODO Auto-generated method stub
	}
}
