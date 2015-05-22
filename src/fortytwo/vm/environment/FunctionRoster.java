package fortytwo.vm.environment;

import java.util.HashMap;
import java.util.List;

import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.library.standard.FunctionFieldAccess;
import fortytwo.library.standard.StdLib42;
import fortytwo.vm.constructions.Function42;
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
	public void add(Function42 function) {
		functions.put(function.signature(), function);
	}
	public static FunctionRoster getDefault() {
		FunctionRoster funcs = new FunctionRoster();
		StdLib42.defaultFunctions(funcs);
		return funcs;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((functions == null) ? 0 : functions.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		FunctionRoster other = (FunctionRoster) obj;
		if (functions == null) {
			if (other.functions != null) return false;
		} else if (!functions.equals(other.functions)) return false;
		return true;
	}
}
