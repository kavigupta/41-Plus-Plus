package fortytwo.vm.errors;

import java.util.ArrayList;
import java.util.List;

import lib.standard.collections.Pair;
import fortytwo.compiler.Context;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.GenericType;
import fortytwo.language.type.TypeVariable;
import fortytwo.library.standard.FunctionFieldAccess;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.expressions.BinaryOperation;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralExpression;

public class CriticalErrors {
	public static void assignedVariableBeingAssigned(
			ArrayList<Pair<VariableIdentifier, Expression>> pairs,
			VariableIdentifier name, Expression express) {
		// TODO 0 implement
	}
	public static void unassignedVariableBeingDeregistered(
			ArrayList<Pair<VariableIdentifier, Expression>> pairs,
			VariableIdentifier name) {
		// TODO 0 implement
	}
	public static void unassignedVariableBeingReferenced(
			ArrayList<Pair<VariableIdentifier, Expression>> pairs,
			VariableIdentifier id) {
		// TODO 0 implement
	}
	public static void unassignedVariableBeingReassigned(
			ArrayList<Pair<VariableIdentifier, Expression>> pairs,
			VariableIdentifier name, Expression express) {
		// TODO 0 implement
	}
	public static void unrecognizedOperator(BinaryOperation binaryOperation,
			Context context) {
		// TODO 0 implement
	}
	public static void assignedTypeVariableBeingReassigned(TypeVariable name,
			ConcreteType express) {
		// TODO 0 implement
	}
	public static void wrongNumberOfArguments(Function42 function42,
			List<LiteralExpression> arguments) {
		// TODO Auto-generated method stub
	}
	public static void nonConcreteTypeIn(FunctionName name,
			List<VariableIdentifier> parameterVariables,
			List<GenericType> parameterTypes, int i) {
		// TODO Auto-generated method stub
	}
	public static void fieldAccessOnNonObject(
			FunctionFieldAccess functionFieldAccess,
			List<LiteralExpression> arguments) {
		// TODO Auto-generated method stub
	}
	public static void uncompiledVariableBeingResolved(
			VariableIdentifier variableIdentifier) {
		// TODO Auto-generated method stub
	}
}
