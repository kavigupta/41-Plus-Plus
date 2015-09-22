package fortytwo.vm.environment;

import java.util.List;
import java.util.Optional;

import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.FunctionType;
import fortytwo.library.standard.StdLib42;
import fortytwo.vm.errors.DNEErrors;

public class TypeEnvironment {
	private final Optional<TypeEnvironment> container;
	public final StructureRoster structs;
	public final FunctionSignatureRoster funcs;
	private final VariableTypeRoster types;
	public static TypeEnvironment getDefault() {
		final StructureRoster structs = StdLib42.DEF_STRUCT;
		final FunctionSignatureRoster funcs = new FunctionSignatureRoster();
		StdLib42.defaultSignatures(funcs);
		final VariableTypeRoster types = new VariableTypeRoster();
		return new TypeEnvironment(structs, funcs, types);
	}
	public static TypeEnvironment getChild(TypeEnvironment environment) {
		return new TypeEnvironment(environment);
	}
	private TypeEnvironment(TypeEnvironment env) {
		this.structs = env.structs;
		this.funcs = new FunctionSignatureRoster();
		this.types = new VariableTypeRoster();
		this.container = Optional.of(env);
	}
	private TypeEnvironment(StructureRoster structureRoster,
			FunctionSignatureRoster sigRost, VariableTypeRoster types) {
		this.structs = structureRoster;
		this.funcs = sigRost;
		this.types = types;
		this.container = Optional.empty();
	}
	public void addType(VariableIdentifier variableIdentifier,
			ConcreteType concreteType) {
		types.add(variableIdentifier, concreteType);
	}
	public ConcreteType typeOf(VariableIdentifier name) {
		final ConcreteType type = types.typeOf(name);
		if (type != null) return type;
		if (!container.isPresent()) DNEErrors.variableDNE(name);
		return container.get().typeOf(name);
	}
	public Optional<FunctionType> referenceTo(FunctionName name,
			List<ConcreteType> types) {
		final Optional<FunctionType> sig = funcs.typeof(name, types);
		if (sig.isPresent()) return sig;
		System.out.println(this.funcs);
		if (!container.isPresent()) DNEErrors.functionSignatureDNE(name, types);
		return container.get().referenceTo(name, types);
	}
	public void putReference(VariableIdentifier id, FunctionType type) {
		funcs.putReference(id, type);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((container == null) ? 0 : container.hashCode());
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
		TypeEnvironment other = (TypeEnvironment) obj;
		if (container == null) {
			if (other.container != null) return false;
		} else if (!container.equals(other.container)) return false;
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
