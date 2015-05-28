package fortytwo.vm.errors;

import java.util.ArrayList;

import lib.standard.collections.Pair;
import fortytwo.compiler.Context;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.TypeVariable;
import fortytwo.vm.expressions.BinaryOperation;
import fortytwo.vm.expressions.Expression;

public class CriticalErrors {
	public static void assignedVariableBeingAssigned(
			ArrayList<Pair<VariableIdentifier, Expression>> pairs,
			VariableIdentifier name, Expression express) {
		// TODO Auto-generated method stub
	}
	public static void unassignedVariableBeingDeregistered(
			ArrayList<Pair<VariableIdentifier, Expression>> pairs,
			VariableIdentifier name) {
		// TODO Auto-generated method stub
	}
	public static void unassignedVariableBeingReferenced(
			ArrayList<Pair<VariableIdentifier, Expression>> pairs,
			VariableIdentifier id) {
		// TODO Auto-generated method stub
	}
	public static void unassignedVariableBeingReassigned(
			ArrayList<Pair<VariableIdentifier, Expression>> pairs,
			VariableIdentifier name, Expression express) {
		// TODO Auto-generated method stub
	}
	public static void unrecognizedOperator(BinaryOperation binaryOperation,
			Context context) {
		// TODO Auto-generated method stub
	}
	public static void assignedTypeVariableBeingReassigned(TypeVariable name,
			ConcreteType express) {
		// TODO Auto-generated method stub
	}
}
