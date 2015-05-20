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
		for (FunctionSignature f : funcs)
			if (f.name.equals(name) && f.accepts(inputs)) return f;
		throw new RuntimeException(/* LOWPRI-E */);
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
}
