package fortytwo.vm.errors;

import fortytwo.compiler.Context;
import fortytwo.language.field.Field;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ArrayType;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.GenericStructureType;
import fortytwo.language.type.TypeVariable;
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
}
