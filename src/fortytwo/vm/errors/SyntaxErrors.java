package fortytwo.vm.errors;

import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token;
import fortytwo.language.Language;
import fortytwo.language.classification.ExpressionType;
import fortytwo.language.classification.SentenceType;

public class SyntaxErrors {
	public static void matchingSymbolDNE(Context parent, String input, int i) {
		Errors.error(ErrorType.SYNTAX, String.format(
				"The closing %s to this %s was not found", input.charAt(i),
				Language.matchingSymbol(input.charAt(i))), parent
				.subContext(i, i + 1));
	}
	public static void invalidExpression(ExpressionType type,
			List<Token> currentExpression) {
		Errors.error(
				ErrorType.SYNTAX,
				String.format("%s is not a valid %s",
						currentExpression.stream().map(x -> x.token)
								.reduce((x, y) -> x + " " + y),
						type.description()),
				Context.tokenSum(currentExpression));
	}
	public static void invalidSentence(SentenceType type, List<Token> line) {
		Errors.error(
				ErrorType.SYNTAX,
				String.format(
						"%s is not a valid %s",
						line.stream().map(x -> x.token)
								.reduce((x, y) -> x + " " + y),
						type.description()), Context.tokenSum(line));
	}
}
