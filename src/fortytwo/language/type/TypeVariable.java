package fortytwo.language.type;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token42;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.errors.DNEErrors;

public class TypeVariable implements GenericType {
	public static final TypeVariable LENGTH = new TypeVariable(
			VariableIdentifier.getInstance(new Token42("\"length\"",
					Context.SYNTHETIC)));
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
	public TypeVariableRoster match(ConcreteType toMatch) {
		TypeVariableRoster roster = new TypeVariableRoster();
		// doesn't worry about reassigning for obvious reasons
		roster.assign(this, toMatch);
		return roster;
	}
	@Override
	public ConcreteType resolve(TypeVariableRoster roster) {
		ConcreteType type = roster.referenceTo(this);
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
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		TypeVariable other = (TypeVariable) obj;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		return true;
	}
}
