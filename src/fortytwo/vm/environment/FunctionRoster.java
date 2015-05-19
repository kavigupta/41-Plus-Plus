package fortytwo.vm.environment;

import java.util.HashMap;
import java.util.List;

import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.library.standard.StdLib42;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.constructions.FunctionFieldAccess;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralObject;

public class FunctionRoster {
	public final HashMap<FunctionSignature, Function42> functions = new HashMap<>();
	private FunctionRoster() {}
	public Function42 get(FunctionSignature signature, List<Expression> inputs) {
		if (StdLib42.matchesFieldAccess(signature.name)) {
			if (!(inputs.get(0) instanceof VariableIdentifier))
				throw new RuntimeException(/* LOWPRI-E */);
			if (!(inputs.get(1) instanceof LiteralObject))
				throw new RuntimeException(/* LOWPRI-E */);
			return new FunctionFieldAccess(
					(VariableIdentifier) inputs.get(0),
					((LiteralObject) inputs.get(1)).struct);
		}
		return functions.get(signature);
	}
	public static FunctionRoster getDefault() {
		FunctionRoster funcs = new FunctionRoster();
		StdLib42.defaultFunctions(funcs);
		return funcs;
	}
}
