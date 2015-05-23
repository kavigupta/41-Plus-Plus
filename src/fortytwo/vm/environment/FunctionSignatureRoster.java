package fortytwo.vm.environment;

import java.util.ArrayList;
import java.util.List;

import fortytwo.compiler.parsed.declaration.FunctionDefinition;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.ConcreteType;

public class FunctionSignatureRoster {
	public final ArrayList<FunctionSignature> funcs = new ArrayList<>();
	public FunctionSignature referenceTo(FunctionName name,
			List<ConcreteType> inputs) {
		for (FunctionSignature f : funcs) {
			if (f.name.equals(name)) {
				if (f.accepts(inputs)) return f;
			}
		}
		throw new RuntimeException(/* LOWPRI-E */name.toString());
	}
	public void putReference(FunctionDefinition f) {
		funcs.add(FunctionSignature.getInstance(f.name, f.parameterTypes,
				f.outputType));
	}
	@Override
	public FunctionSignatureRoster clone() {
		FunctionSignatureRoster other = new FunctionSignatureRoster();
		other.funcs.addAll(funcs);
		return other;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((funcs == null) ? 0 : funcs.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		FunctionSignatureRoster other = (FunctionSignatureRoster) obj;
		if (funcs == null) {
			if (other.funcs != null) return false;
		} else if (!funcs.equals(other.funcs)) return false;
		return true;
	}
}
