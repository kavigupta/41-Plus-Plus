package fortytwo.vm.environment;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.ConcreteType;

public class FunctionSignatureRoster {
	private final HashSet<FunctionSignature> typeMap;
	public FunctionSignatureRoster() {
		this.typeMap = new HashSet<>();
	}
	public Optional<FunctionSignature> referenceTo(FunctionName name,
			List<ConcreteType> inputs) {
		for (final FunctionSignature f : typeMap)
			if (f.name.equals(name))
				if (f.type.accepts(inputs)) return Optional.of(f);
		return Optional.empty();
	}
	public void putReference(FunctionSignature sig) {
		typeMap.add(sig);
	}
	@Override
	public FunctionSignatureRoster clone() {
		final FunctionSignatureRoster other = new FunctionSignatureRoster();
		other.typeMap.addAll(typeMap);
		return other;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((typeMap == null) ? 0 : typeMap.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		FunctionSignatureRoster other = (FunctionSignatureRoster) obj;
		if (typeMap == null) {
			if (other.typeMap != null) return false;
		} else if (!typeMap.equals(other.typeMap)) return false;
		return true;
	}
	@Override
	public String toString() {
		return "FunctionSignatureRoster [typeMap=" + typeMap + "]";
	}
}
