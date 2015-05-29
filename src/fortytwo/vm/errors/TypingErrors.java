package fortytwo.vm.errors;

import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token;
import fortytwo.language.field.Field;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.*;
import fortytwo.vm.constructions.Structure;
import fortytwo.vm.environment.StructureRoster;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.environment.VariableRoster;
import fortytwo.vm.expressions.BinaryOperation;
import fortytwo.vm.expressions.Expression;

public class TypingErrors {
	public static void nonNumberInArithmeticOperator(
			BinaryOperation operation, int whichArgument, Context context) {
		// TODO 0 implement
	}
	public static void nonBoolInWhileLoopDeclr(Expression condition) {
		// TODO 0 implement
	}
	public static void fieldAccessCalledOnPrimitive(ConcreteType type,
			VariableRoster<?> fields) {
		// TODO 0 implement
	}
	public static void structureNotFound(GenericStructureType type,
			StructureRoster structureRoster) {
		// TODO 0 implement
	}
	public static void fieldNotFound(ConcreteType type,
			VariableIdentifier field, StructureRoster structureRoster) {
		// TODO 0 implement
	}
	public static void nonLengthFieldAccessOnArray(ArrayType type,
			VariableRoster<?> fieldValues) {
		// TODO 0 implement
	}
	public static void fieldAccessCalledOnPrimitive(ConcreteType type,
			VariableIdentifier field) {
		// TODO 0 implement
	}
	public static void fieldAssignmentTypeMismatch(Expression obj,
			Field field, Expression value) {
		// TODO Auto-generated method stub
	}
	public static void variableNotInRoster(TypeVariable typeVariable,
			TypeVariableRoster roster) {
		// TODO Auto-generated method stub
	}
	public static void fieldAssignmentTypeMismatch(Structure struct, Field f,
			ConcreteType type) {
		// TODO Auto-generated method stub
	}
	public static void fieldAssignmentIncomplete(Structure struct,
			VariableRoster<?> fields) {
		// TODO Auto-generated method stub
	}
	public static void nonConcreteTypeInVariableDeclaration(List<Token> line,
			GenericType type) {
		// TODO Auto-generated method stub
	}
	public static void redefinitionTypeMismatch(VariableIdentifier name,
			Expression value) {
		// TODO Auto-generated method stub
	}
	public static void ifConditionNonBool(Expression condition) {
		// TODO Auto-generated method stub
	}
	public static void incompleteTypeVariableSystem(
			GenericStructureType genericStructureType,
			TypeVariableRoster roster) {
		// TODO Auto-generated method stub
	}
	public static void functionSigtypeMismatch(FunctionName name,
			List<? extends Expression> arguments, int i) {
		// TODO Auto-generated method stub
	}
}
