package fortytwo.vm.environment;

import java.util.HashMap;

import fortytwo.language.identifier.FunctionSignature;
import fortytwo.vm.constructions.Function42;

public class FunctionRoster {
	public final HashMap<FunctionSignature, Function42> functions = new HashMap<>();
	public Function42 get(FunctionSignature signature) {
		return functions.get(signature);
	}
}
