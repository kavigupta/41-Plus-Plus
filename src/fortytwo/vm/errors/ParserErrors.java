package fortytwo.vm.errors;

import java.util.List;

import fortytwo.compiler.Token;
import fortytwo.compiler.parsed.declaration.FunctionDefinition;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.compiler.parsed.sentences.Sentence;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.GenericType;

public class ParserErrors {
	public static void nonVariableInDecl(boolean functionDecl, Token x,
			List<Token> line) {
		// TODO Auto-generated method stub
	}
	/**
	 * Use -1 for {@code argument} to signify output type. The rest signify
	 * input type.
	 */
	public static void genericTypeInFunctionDecl(GenericType type,
			List<Token> line, int argument) {
		// TODO Auto-generated method stub
	}
	public static void incompleteFields(VariableIdentifier vid,
			List<Token> line) {
		// TODO Auto-generated method stub
	}
	public static void genericTypeInStructConstructor(Token token) {
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
}
