package fortytwo.vm.errors;

import java.util.List;
import java.util.stream.Collectors;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token42;
import fortytwo.language.Language;
import fortytwo.language.ParsedConstruct;
import fortytwo.language.SourceCode;
import fortytwo.language.field.Field;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.GenericType;
import fortytwo.language.type.PrimitiveTypeWithoutContext;
import fortytwo.vm.VirtualMachine;
import fortytwo.vm.constructions.Structure;
import fortytwo.vm.environment.VariableRoster;
import fortytwo.vm.expressions.BinaryOperation;
import fortytwo.vm.expressions.Expression;

public class TypingErrors {
	public static void typeError(String expressionDescription,
			String expectedType, Expression actual) {
		/*
		 * <Expression description> must be <expected type>, but is actually
		 * ~<expression>~, which is <actual type>.
		 */
		VirtualMachine.error(ErrorType.TYPING, String.format(
				"%s must be %s, but is actually ~%s~, which is %s",
				expressionDescription, Language.articleized(expectedType),
				actual.toSourceCode(),
				Language.articleized(actual.resolveType().toSourceCode())),
				actual.context());
	}
	public static void expectedNumberInArithmeticOperator(
			BinaryOperation operation, boolean firstArgument) {
		typeError("The " + (firstArgument ? "first" : "second")
				+ " argument in " + operation.operation.noun.toLowerCase(),
				PrimitiveTypeWithoutContext.NUMBER.toSourceCode(),
				firstArgument ? operation.first : operation.second);
	}
	public static void expectedBoolInCondition(boolean ifIf,
			Expression condition) {
		typeError("The condition of " + (ifIf ? "an if" : "a while")
				+ " loop", PrimitiveTypeWithoutContext.BOOL.toSourceCode(),
				condition);
	}
	public static void redefinitionTypeMismatch(Field name, Expression value) {
		typeError("The value of " + name.name.toSourceCode(),
				name.type.toSourceCode(), value);
	}
	public static void fieldAssignmentTypeMismatch(Structure struct,
			Field field, Expression value) {
		typeError("The " + field.name.toSourceCode() + " of "
				+ struct.type.toSourceCode(), field.type.toSourceCode(),
				value);
	}
	public static void incompleteConstructor(Structure struct,
			VariableRoster<?> fields) {
		String actual = "no fields";
		if (fields.size() != 0) {
			String displayedFields = SourceCode.displayList(struct.fields
					.stream().map(ParsedConstruct::toSourceCode)
					.collect(Collectors.toList()));
			actual = "only " + displayedFields;
		}
		actual += (fields.size() == 1 ? "was" : "were");
		String msg = String
				.format("The structure %s must contain the field%s %s, but %s defined here.",
						struct.type.toSourceCode(),
						struct.fields.size() == 1 ? "" : "s",
						SourceCode.displayList(struct.fields.stream()
								.map(ParsedConstruct::toSourceCode)
								.collect(Collectors.toList())), actual);
		VirtualMachine.error(
				ErrorType.PARSING,
				msg,
				Context.sum(struct.fields.stream().map(x -> x.context())
						.collect(Collectors.toList())));
	}
	public static void incompleteArrayConstructor(Context context) {
		VirtualMachine
				.error(ErrorType.PARSING,
						"Arrays must be defined with the field _length, but it was not defined here",
						context);
	}
	public static void inresolubleType(GenericType gt) {
		VirtualMachine
				.error(ErrorType.PARSING,
						String.format(
								"The type ~%s~ could not be understood given the type variables known here",
								gt.toSourceCode()), gt.context());
	}
	public static void invalidArrayType(List<Token42> tokens,
			List<GenericType> typeVariables) {
		String contentType;
		if (typeVariables.size() == 0)
			contentType = "no content type";
		else {
			contentType = "the content types "
					+ SourceCode.displayList(typeVariables.stream()
							.map(ParsedConstruct::toSourceCode)
							.collect(Collectors.toList()));
		}
		VirtualMachine
				.error(ErrorType.PARSING,
						String.format(
								"Arrays must be defined in terms of "
										+ "exactly one type of content, but ~%s~ is defined with %s",
								tokens.stream()
										.map(x -> x.token)
										.reduce((a, b) -> a + " " + b)
										.get(), contentType), Context
								.tokenSum(tokens));
	}
	public static void incompleteFieldTypingInFunctionDecl(
			VariableIdentifier vid, List<Token42> line) {
		VirtualMachine
				.error(ErrorType.PARSING,
						String.format(
								"The type of the variable ~%s~ is not specified in this function declaration",
								vid.toSourceCode()), vid.context());
	}
	public static void noValue(Field name) {
		VirtualMachine
				.error(ErrorType.PARSING,
						String.format(
								"The primitive variable ~%s~, of type ~%s~, cannot be defined without a value",
								name.name.toSourceCode(),
								name.type.toSourceCode()), name
								.context());
	}
}
