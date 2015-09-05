package fortytwo.vm.environment;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.ConcreteType;
import fortytwo.library.standard.StdLib42;
import fortytwo.vm.constructions.Function42;

public class FunctionRoster {
	private final StaticEnvironment env;
	public final HashMap<FunctionSignature, Function42> functions = new HashMap<>();
	private FunctionRoster(StaticEnvironment env) {
		this.env = env;
	}
	public Optional<Function42> get(FunctionSignature signature,
			List<Expression> arguments, List<ConcreteType> types) {
		final Pair<Function42, ConcreteType> func = StdLib42
				.matchCompiledFieldAccess(env, signature.name, types);
		if (func != null) return Optional.of(func.getKey());
		final Function42 f = functions.get(signature);
		return f == null ? Optional.empty() : Optional.of(f);
	}
	public void add(Function42 function) {
		functions.put(function.signature(), function);
	}
	public static FunctionRoster getDefault(StaticEnvironment env) {
		final FunctionRoster funcs = new FunctionRoster(env);
		StdLib42.defaultFunctions(funcs);
		return funcs;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (functions == null ? 0 : functions.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final FunctionRoster other = (FunctionRoster) obj;
		if (functions == null) {
			if (other.functions != null) return false;
		} else if (!functions.equals(other.functions)) return false;
		return true;
	}
}
