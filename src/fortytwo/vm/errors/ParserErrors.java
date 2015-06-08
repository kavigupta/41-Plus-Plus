package fortytwo.vm.errors;

import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token;
import fortytwo.compiler.parsed.declaration.FunctionDefinition;
import fortytwo.compiler.parsed.expressions.UntypedExpression;
import fortytwo.compiler.parsed.sentences.Sentence;
import fortytwo.language.Language;
import fortytwo.language.type.GenericType;
import fortytwo.vm.VirtualMachine;

public class ParserErrors {
	public static void expectedVariableInDecl(boolean functionDecl,
			Token problem, List<Token> line) {
		VirtualMachine.error(ErrorType.PARSING, String.format(
				"A %s definition must contain only name tokens and variables,"
						+ " but ~%s~ is neither.",
				functionDecl ? "function" : "type", problem.token),
				problem.context);
	}
	/**
	 * Use -1 for {@code argument} to signify output type. The rest signify
	 * input type.
	 */
	public static void expectedCTInFunctionDecl(GenericType type,
			List<Token> line, int argument) {
		String location = argument < 0 ? "output" : (Language
				.ordinal(argument) + " input");
		VirtualMachine.error(
				ErrorType.PARSING,
				String.format(
						"Function declarations must only include concrete types;"
								+ " however, the type ~%s~, the %s of the function, is generic.",
						type.toSourceCode(), location), type.context());
	}
	public static void expectedCTInDefinition(GenericType type) {
		VirtualMachine.error(ErrorType.PARSING, String.format(
				"Only variables of concrete types can be declarated;"
						+ " however, the type ~%s~ is generic",
				type.toSourceCode()), type.context());
	}
	public static void noExit(FunctionDefinition f) {
		unresolvedTermination("function definition", "function output",
				f.context());
	}
	public static void noCloseVB(List<Token> openingBrace) {
		unresolvedTermination("Open block", "Close Block",
				Context.tokenSum(openingBrace));
	}
	public static void unresolvedTermination(String startType, String endType,
			Context context) {
		VirtualMachine.error(ErrorType.PARSING, String.format(
				"The %s for this %s was not found.", endType, startType),
				context);
	}
	public static void expectedFunctionCall(UntypedExpression exp) {
		expected("function call", exp);
	}
	public static void expectedStatement(Sentence x) {
		expected("statement", x);
	}
	public static void expectedDeclarationOrDefinition(Sentence s) {
		expected("declaration or definition", s);
	}
	public static void expectedVariableInFieldAccess(
			UntypedExpression parsedExpression) {
		expected("variable", parsedExpression);
	}
	public static void expected(String expectedClass, Sentence actually) {
		VirtualMachine.error(
				ErrorType.PARSING,
				String.format(
						"%s was expected here, but instead what was received was ~%s~, %s.",
						Language.uppercase(Language
								.articleized(expectedClass)), actually
								.toSourceCode(),
						Language.articleized(actually.type().description)),
				actually.context());
	}
}
