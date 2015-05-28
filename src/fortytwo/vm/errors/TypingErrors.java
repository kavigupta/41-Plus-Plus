package fortytwo.vm.errors;

import fortytwo.compiler.Context;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ArrayType;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.GenericStructureType;
import fortytwo.vm.environment.StructureRoster;
import fortytwo.vm.environment.VariableRoster;
import fortytwo.vm.expressions.BinaryOperation;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralExpression;

public class TypingErrors {
	public static void nonNumberInArithmeticOperator(
			BinaryOperation operation, int whichArgument, Context context) {
		// TODO Auto-generated method stub
	}
	public static void nonBoolInWhileLoopDeclr(Expression condition) {
		// TODO Auto-generated method stub
	}
	public static void fieldAccessCalledOnPrimitive(ConcreteType type,
			VariableRoster<?> fields) {
		// TODO Auto-generated method stub
	}
	public static void structureNotFound(GenericStructureType type,
			StructureRoster structureRoster) {
		// TODO Auto-generated method stub
	}
	public static void fieldNotFound(ConcreteType type,
			VariableIdentifier field, StructureRoster structureRoster) {
		// TODO Auto-generated method stub
	}
	public static void nonLengthFieldAccessOnArray(ArrayType type,
			VariableRoster<LiteralExpression> fieldValues) {
		// TODO Auto-generated method stub
	}
	public static void fieldAccessCalledOnPrimitive(ConcreteType type,
			VariableIdentifier field) {
		// TODO Auto-generated method stub
	}
}
