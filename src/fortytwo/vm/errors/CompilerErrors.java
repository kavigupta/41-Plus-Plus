package fortytwo.vm.errors;

import java.util.HashMap;
import java.util.List;

import fortytwo.compiler.Token;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.TypeVariable;

public class CompilerErrors {
	public static void expectedStatementButReceivedExpression(List<Token> list) {
		// TODO 0 implement
	}
	public static void variableNotFound(TypeVariable id,
			HashMap<TypeVariable, ConcreteType> pairs) {
		// TODO Auto-generated method stub
	}
}
