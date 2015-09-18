package fortytwo.vm.environment;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.library.standard.StdLib42;
import fortytwo.vm.expressions.LiteralFunction;

public class FunctionRoster {
	public final HashMap<VariableIdentifier, LiteralFunction> functions = new HashMap<>();
	private FunctionRoster() {}
	public Optional<LiteralFunction> get(FunctionSignature signature,
			List<ConcreteType> types) {
		// final Optional<LiteralFunction> func = StdLib42
		// .matchCompiledFieldAccess(env, signature.name, types);
		// if (func.isPresent()) return func;
		for (final Entry<VariableIdentifier, LiteralFunction> x : functions
				.entrySet()) {
			if (!x.getKey().unmangledName()
					.equals(signature.name.identifier().unmangledName()))
				continue;
			if (!x.getValue().type.accepts(types)) continue;
			return Optional.of(x.getValue());
		}
		return Optional.empty();
	}
	public void add(VariableIdentifier sig, LiteralFunction func) {
		functions.put(sig, func);
	}
	public static FunctionRoster getDefault() {
		final FunctionRoster funcs = new FunctionRoster();
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
