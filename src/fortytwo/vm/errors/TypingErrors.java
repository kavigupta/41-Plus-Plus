package fortytwo.vm.errors;

import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token;
import fortytwo.language.Language;
import fortytwo.language.field.Field;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.GenericType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypeWithoutContext;
import fortytwo.vm.constructions.Structure;
import fortytwo.vm.environment.VariableRoster;
import fortytwo.vm.expressions.BinaryOperation;
import fortytwo.vm.expressions.Expression;

public class TypingErrors {
	public static void typeError(String expressionDescription,
			ConcreteType expected, Expression actual) {
		/*
		 * <Expression description> must be <expected type>, but is actually
		 * ~<expression>~, which is <actual type>.
		 */
		Errors.error(ErrorType.TYPING, String.format(
				"%s must be %s, but is actually ~%s~, which is %s",
				expressionDescription,
				Language.articleized(expected.toSourceCode()),
				actual.toSourceCode(),
				Language.articleized(actual.resolveType().toSourceCode())),
				actual.context());
	}
	public static void expectedNumberInArithmeticOperator(
			BinaryOperation operation, boolean firstArgument) {
		typeError("The " + (firstArgument ? "first" : "second")
				+ " argument in " + operation.operation.noun.toLowerCase(),
				new PrimitiveType(PrimitiveTypeWithoutContext.NUMBER, Context
						.synthetic()), firstArgument ? operation.first
						: operation.second);
	}
	public static void expectedBoolInCondition(boolean ifIf,
			Expression condition) {
		typeError("The condition of " + (ifIf ? "an if" : "a while")
				+ " loop",
				new PrimitiveType(PrimitiveTypeWithoutContext.BOOL, Context.synthetic()),
				condition);
	}
	public static void redefinitionTypeMismatch(Field name, Expression value) {
		typeError("The value of " + name.name.toSourceCode(), name.type,
				value);
	}
	public static void fieldAssignmentTypeMismatch(Structure struct,
			Field field, Expression value) {
		typeError("The " + field.name.toSourceCode() + " of "
				+ struct.type.toSourceCode(), field.type, value);
	}
	public static void incompleteConstructor(Structure struct,
			VariableRoster<?> fields) {
		// TODO 0 implement
	}
	public static void incompleteArrayConstructor(Context context) {
		// TODO Auto-generated method stub
	}
	public static void inresolubleType(GenericType gt) {
		// TODO Auto-generated method stub
	}
	public static void invalidArrayType(List<Token> tokens,
			List<GenericType> typeVariables) {
		// TODO Auto-generated method stub
	}
}
