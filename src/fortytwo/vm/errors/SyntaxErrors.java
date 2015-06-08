package fortytwo.vm.errors;

import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token;
import fortytwo.language.Language;
import fortytwo.language.classification.ExpressionType;
import fortytwo.language.classification.SentenceType;
import fortytwo.vm.VirtualMachine;

public class SyntaxErrors {
	public static void matchingSymbolDNE(Context parent, String input, int i) {
		VirtualMachine.error(
				ErrorType.SYNTAX,
				String.format("This ~%s~ has no corresponding ~%s~",
						input.charAt(i),
						Language.matchingSymbol(input.charAt(i))),
				parent.subContext(i, i + 1));
	}
	public static void invalidExpression(ExpressionType type,
			List<Token> currentExpression) {
		VirtualMachine.error(
				ErrorType.SYNTAX,
				String.format("%s is not a valid %s",
						currentExpression.stream().map(x -> x.token)
								.reduce((x, y) -> x + " " + y),
						type.description()),
				Context.tokenSum(currentExpression));
	}
	public static void invalidSentence(SentenceType type, List<Token> line) {
		VirtualMachine.error(
				ErrorType.SYNTAX,
				String.format(
						"%s is not a valid %s",
						line.stream().map(x -> x.token)
								.reduce((x, y) -> x + " " + y),
						type.description()), Context.tokenSum(line));
	}
}
