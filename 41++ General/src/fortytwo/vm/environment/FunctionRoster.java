package fortytwo.vm.environment;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import lib.standard.collections.Pair;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.ConcreteType;
import fortytwo.library.standard.StdLib42;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.expressions.Expression;

public class FunctionRoster {
	private final StaticEnvironment env;
	public final HashMap<FunctionSignature, Function42> functions = new HashMap<>();
	private FunctionRoster(StaticEnvironment env) {
		this.env = env;
	}
	public Function42 get(FunctionSignature signature, List<Expression> inputs) {
		Pair<Function42, ConcreteType> func = StdLib42
				.matchCompiledFieldAccess(env, signature.name,
						inputs.stream().map(Expression::resolveType)
								.collect(Collectors.toList()));
		if (func != null) return func.getKey();
		return functions.get(signature);
	}
	public void add(Function42 function) {
		functions.put(function.signature(), function);
	}
	public static FunctionRoster getDefault(StaticEnvironment env) {
		FunctionRoster funcs = new FunctionRoster(env);
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
