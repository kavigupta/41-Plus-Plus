package fortytwo.language.type;

import java.util.Optional;

import fortytwo.compiler.Context;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.errors.DNEErrors;

public class TypeVariable implements GenericType {
	public final VariableIdentifier name;
	public TypeVariable(VariableIdentifier name) {
		this.name = name;
	}
	@Override
	public Kind kind() {
		return Kind.VARIABLE;
	}
	@Override
	public Context context() {
		return name.context();
	}
	@Override
	public Optional<TypeVariableRoster> match(ConcreteType toMatch) {
		final TypeVariableRoster roster = new TypeVariableRoster();
		// doesn't worry about reassigning for obvious reasons
		roster.assign(this, toMatch);
		return Optional.of(roster);
	}
	@Override
	public ConcreteType resolve(TypeVariableRoster roster) {
		final ConcreteType type = roster.referenceTo(this);
		if (type == null) DNEErrors.typeDNE(this);
		return type;
	}
	@Override
	public String toSourceCode() {
		return name.toSourceCode();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (name == null ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final TypeVariable other = (TypeVariable) obj;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		return true;
	}
}
