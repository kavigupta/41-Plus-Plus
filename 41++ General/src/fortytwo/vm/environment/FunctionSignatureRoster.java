package fortytwo.vm.environment;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.FunctionType;

public class FunctionSignatureRoster {
	private final HashMap<VariableIdentifier, ConcreteType> typeMap;
	public FunctionSignatureRoster() {
		this.typeMap = new HashMap<>();
	}
	public Optional<FunctionType> typeof(FunctionName name,
			List<ConcreteType> inputs) {
		for (final Entry<VariableIdentifier, ConcreteType> f : typeMap
				.entrySet()) {
			final String unmangled = f.getKey().unmangledName();
			if (!unmangled.equals(name.identifier().name.token)) continue;
			if (!(f.getValue() instanceof FunctionType)) continue;
			if (((FunctionType) f.getValue()).accepts(inputs))
				return Optional.of((FunctionType) f.getValue());
		}
		return Optional.empty();
	}
	public void putReference(VariableIdentifier id, ConcreteType type) {
		typeMap.put(id, type);
	}
	@Override
	public FunctionSignatureRoster clone() {
		final FunctionSignatureRoster other = new FunctionSignatureRoster();
		other.typeMap.putAll(typeMap);
		return other;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (typeMap == null ? 0 : typeMap.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final FunctionSignatureRoster other = (FunctionSignatureRoster) obj;
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
