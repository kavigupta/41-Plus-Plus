package fortytwo.vm.errors;

import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.language.field.Field;
import fortytwo.language.identifier.FunctionName;
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
	public static void expectedNumberInArithmeticOperator(
			BinaryOperation operation, int whichArgument, Context context) {
		// TODO 0 implement
	}
	public static void expectedBoolInWhileLoopCond(Expression condition) {
		// TODO 0 implement
	}
	public static void fieldAccessOnPrimitive(ConcreteType type,
			List<VariableIdentifier> field) {
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
	public static void variableNotInRoster(TypeVariable typeVariable,
			TypeVariableRoster roster) {
		// TODO Auto-generated method stub
	}
	public static void fieldAssignmentTypeMismatch(Structure struct,
			Field field, Expression value) {
		// TODO Auto-generated method stub
	}
	public static void fieldAssignmentIncomplete(Structure struct,
			VariableRoster<?> fields) {
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
