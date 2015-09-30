package fortytwo.vm.environment.type;

import java.util.List;
import java.util.Optional;

import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.FunctionType;

public abstract class AbstractTypeEnvironment {
	public final TypeRoster typeRoster;
	public AbstractTypeEnvironment(TypeRoster typeRoster) {
		this.typeRoster = typeRoster;
	}
	public void addType(VariableIdentifier variableIdentifier,
			ConcreteType concreteType) {
		typeRoster.addType(variableIdentifier, concreteType);
	}
	public void putReference(VariableIdentifier id, FunctionType type) {
		typeRoster.putReference(id, type);
	}
	public ConcreteType typeOf(VariableIdentifier name) {
		final Optional<ConcreteType> type = typeRoster.typeOf(name);
		if (type.isPresent()) return type.get();
		return checkParentForTypeOf(name);
	}
	public FunctionType referenceTo(FunctionName name,
			List<ConcreteType> types) {
		final Optional<FunctionType> sig = typeRoster.referenceTo(name, types);
		if (sig.isPresent()) return sig.get();
		return checkParentForTypeOf(name, types);
	}
	protected abstract ConcreteType checkParentForTypeOf(
			VariableIdentifier name);
	protected abstract FunctionType checkParentForTypeOf(FunctionName name,
			List<ConcreteType> types);
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((typeRoster == null) ? 0 : typeRoster.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		AbstractTypeEnvironment other = (AbstractTypeEnvironment) obj;
		if (typeRoster == null) {
			if (other.typeRoster != null) return false;
		} else if (!typeRoster.equals(other.typeRoster)) return false;
		return true;
	}
}
