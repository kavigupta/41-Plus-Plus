package fortytwo.vm.errors;

import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token;
import fortytwo.language.classification.ExpressionType;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.type.GenericType;

public class SyntaxErrors {
	public static void closingMarkDNE(Context parent, String input, int i) {
		// TODO 0 implement
	}
	public static void openMarkDNE(Context parent, String input, int i) {
		// TODO 0 implement
	}
	public static void invalidExpression(ExpressionType type,
			List<Token> currentExpression) {
		// TODO Auto-generated method stub
	}
	public static void invalidSentence(SentenceType type, List<Token> line) {
		// TODO Auto-generated method stub
	}
	public static void invalidArrayType(List<Token> tokens,
			List<GenericType> typeVariables) {
		// TODO Auto-generated method stub
	}
}
