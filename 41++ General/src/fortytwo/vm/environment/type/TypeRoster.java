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
	public final VariableTypeRoster types;
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
	public String toString() {
		return "TypeRoster [structs=" + structs + ", funcs=" + funcs
				+ ", types=" + types + "]";
	}
}
