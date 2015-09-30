package fortytwo.vm.errors;

import java.util.List;
import java.util.stream.Collectors;

import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;
import fortytwo.compiler.parsed.ParsedConstruct;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.compiler.parsed.statements.FunctionOutput;
import fortytwo.language.Language;
import fortytwo.language.SourceCode;
import fortytwo.language.field.TypedVariable;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.GenericType;
import fortytwo.language.type.PrimitiveTypeWOC;
import fortytwo.vm.VirtualMachine;
import fortytwo.vm.constructions.Structure;
import fortytwo.vm.environment.TypeEnvironment;
import fortytwo.vm.environment.VariableRoster;

public class TypingErrors {
	public static void typeError(String expressionDescription,
			String expectedType, Expression expression, TypeEnvironment env) {
		/*
		 * <Expression description> must be <expected type>, but is actually
		 * ~<expression>~, which is <actual type>.
		 */
		VirtualMachine.error(ErrorType.TYPING,
				String.format(
						"%s must be %s, but is actually ~%s~, which is %s",
						expressionDescription,
						Language.articleized(expectedType),
						expression.toSourceCode(),
						Language.articleized(
								expression.type(env).toSourceCode())),
				expression.context());
	}
	public static void expectedBoolInCondition(boolean ifIf,
			Expression condition, TypeEnvironment env) {
		typeError("The condition of " + (ifIf ? "an if" : "a while") + " loop",
				PrimitiveTypeWOC.BOOL.toSourceCode(), condition, env);
	}
	public static void redefinitionTypeMismatch(TypedVariable name,
			Expression parsedExpression, TypeEnvironment env) {
		typeError("The value of " + name.name.toSourceCode(),
				name.type.toSourceCode(), parsedExpression, env);
	}
	public static void fieldAssignmentTypeMismatch(Structure struct,
			TypedVariable field, Expression value, TypeEnvironment env) {
		typeError(
				"The " + field.name.toSourceCode() + " of "
						+ struct.type.toSourceCode(),
				field.type.toSourceCode(), value, env);
	}
	public static void incompleteConstructor(Structure struct,
			VariableRoster<? extends Expression> fieldValues) {
		String actual = "no fields";
		if (fieldValues.numberOfVariables() != 0) {
			final String displayedFields = SourceCode.displayList(
					struct.fields.stream().map(ParsedConstruct::toSourceCode)
							.collect(Collectors.toList()));
			actual = "only " + displayedFields;
		}
		actual += fieldValues.numberOfVariables() == 1 ? "was" : "were";
		final String msg = String.format(
				"The structure %s must contain the field%s %s, but %s defined here.",
				struct.type.toSourceCode(),
				struct.fields.size() == 1 ? "" : "s",
				SourceCode.displayList(struct.fields.stream()
						.map(ParsedConstruct::toSourceCode)
						.collect(Collectors.toList())),
				actual);
		VirtualMachine.error(ErrorType.PARSING, msg, Context.sum(struct.fields
				.stream().map(x -> x.context()).collect(Collectors.toList())));
	}
	public static void incompleteArrayConstructor(Context context) {
		VirtualMachine.error(ErrorType.PARSING,
				"Arrays must be defined with the field \"length\", but it was not defined here",
				context);
	}
	public static void inresolubleType(GenericType gt) {
		VirtualMachine.error(ErrorType.PARSING,
				String.format(
						"The type ~%s~ could not be understood given the type variables known here",
						gt.toSourceCode()),
				gt.context());
	}
	public static void invalidArrayType(List<LiteralToken> tokens,
			List<GenericType> typeVariables) {
		String contentType;
		if (typeVariables.size() == 0)
			contentType = "no content type";
		else contentType = "the content types " + SourceCode.displayList(
				typeVariables.stream().map(ParsedConstruct::toSourceCode)
						.collect(Collectors.toList()));
		VirtualMachine.error(ErrorType.PARSING, String.format(
				"Arrays must be defined in terms of "
						+ "exactly one type of content, but ~%s~ is defined with %s",
				tokens.stream().map(x -> x.token).reduce((a, b) -> a + " " + b)
						.get(),
				contentType), Context.sum(tokens));
	}
	public static void incompleteFieldTypingInFunctionDecl(
			VariableIdentifier vid, List<LiteralToken> line) {
		VirtualMachine.error(ErrorType.PARSING,
				String.format(
						"The type of the variable ~%s~ is not specified in this function declaration",
						vid.toSourceCode()),
				vid.context());
	}
	public static void noValue(TypedVariable name) {
		VirtualMachine.error(ErrorType.PARSING,
				String.format(
						"The primitive variable ~%s~, of type ~%s~, cannot be defined without a value",
						name.name.toSourceCode(), name.type.toSourceCode()),
				name.context());
	}
	public static void incorrectOutput(String debugName, GenericType output,
			GenericType actual, FunctionOutput s) {
		VirtualMachine.error(ErrorType.TYPING,
				String.format(
						"The function ~%s~ should output ~%s~, but the sentence ~%s~'s output is of type ~%s~",
						debugName, Language.articleized(output.toSourceCode()),
						s.toSourceCode(), actual.toSourceCode()),
				s.context());
	}
	public static void inconsistentBranchTyping(String inWhat,
			GenericType first, GenericType second, Context context) {
		VirtualMachine.error(ErrorType.TYPING,
				String.format(
						"The two branches of this %s contain outputs of types ~%s~ and ~%s~, when in fact they should return the same type",
						inWhat, first, second),
				context);
	}
}
