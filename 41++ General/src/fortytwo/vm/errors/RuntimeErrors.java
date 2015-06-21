package fortytwo.vm.errors;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.expressions.BinaryOperation;
import fortytwo.vm.expressions.LiteralArray;

public class RuntimeErrors {
	public static void divideByZero(BinaryOperation binaryOperation,
			Context context) {
		// LOWPRI implement later along with general errors
	}
	public static void indexOutOfBoundsException(LiteralArray literalArray,
			int i, Context context) {
		// LOWPRI implement later along with general errors
	}
}
