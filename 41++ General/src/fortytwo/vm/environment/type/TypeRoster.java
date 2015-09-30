package fortytwo.vm.environment.type;

import java.util.List;
import java.util.Optional;

import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.FunctionType;
import fortytwo.library.standard.StdLib42;
import fortytwo.vm.environment.FunctionSignatureRoster;
import fortytwo.vm.environment.StructureRoster;
import fortytwo.vm.environment.VariableTypeRoster;

public class TypeRoster {
	public final StructureRoster structs;
	public final FunctionSignatureRoster funcs;
	private final VariableTypeRoster types;
	public static TypeRoster getDefault(StructureRoster structs) {
		final FunctionSignatureRoster funcs = new FunctionSignatureRoster();
		StdLib42.defaultSignatures(funcs);
		final VariableTypeRoster types = new VariableTypeRoster();
		return new TypeRoster(structs, funcs, types);
	}
	public TypeRoster(StructureRoster structureRoster,
			FunctionSignatureRoster sigRost, VariableTypeRoster types) {
		this.structs = structureRoster;
		this.funcs = sigRost;
		this.types = types;
	}
	public void addType(VariableIdentifier variableIdentifier,
			ConcreteType concreteType) {
		types.add(variableIdentifier, concreteType);
	}
	public Optional<ConcreteType> typeOf(VariableIdentifier name) {
		final ConcreteType type = types.typeOf(name);
		return Optional.ofNullable(type);
	}
	public Optional<FunctionType> referenceTo(FunctionName name,
			List<ConcreteType> types) {
		return funcs.typeof(name, types);
	}
	public void putReference(VariableIdentifier id, FunctionType type) {
		funcs.putReference(id, type);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((funcs == null) ? 0 : funcs.hashCode());
		result = prime * result + ((structs == null) ? 0 : structs.hashCode());
		result = prime * result + ((types == null) ? 0 : types.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		TypeRoster other = (TypeRoster) obj;
		if (funcs == null) {
			if (other.funcs != null) return false;
		} else if (!funcs.equals(other.funcs)) return false;
		if (structs == null) {
			if (other.structs != null) return false;
		} else if (!structs.equals(other.structs)) return false;
		if (types == null) {
			if (other.types != null) return false;
		} else if (!types.equals(other.types)) return false;
		return true;
	}
}
