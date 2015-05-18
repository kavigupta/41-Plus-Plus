package fortytwo.vm.environment;

import java.util.HashMap;
import java.util.List;

import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.constructions.Function42;

public class FunctionRoster {
	public final HashMap<FunctionSignature, Function42> functions = new HashMap<>();
	public Function42 get(FunctionSignature signature) {
		return functions.get(signature);
	}
	public FunctionSignature referenceTo(FunctionName name,
			List<ConcreteType> types) {
		for (FunctionSignature sig : functions.keySet()) {
			if (sig.name.equals(name) && sig.inputTypes.equals(types)) { return sig; }
		}
		throw new RuntimeException(/* LOWPRI-E */);
	}
}
