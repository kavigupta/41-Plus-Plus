package fortytwo.vm.environment;

import java.util.HashMap;

import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.TypeVariable;
import fortytwo.vm.errors.DNEErrors;

public class TypeVariableRoster {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pairs == null) ? 0 : pairs.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		TypeVariableRoster other = (TypeVariableRoster) obj;
		if (pairs == null) {
			if (other.pairs != null) return false;
		} else if (!pairs.equals(other.pairs)) return false;
		return true;
	}
	public final HashMap<TypeVariable, ConcreteType> pairs = new HashMap<>();
	public void assign(TypeVariable name, ConcreteType express) {
		pairs.put(name, express);
	}
	public ConcreteType referenceTo(TypeVariable id) {
		ConcreteType le = pairs.get(id);
		if (le == null) DNEErrors.variableDNE(id);
		return le;
	}
	public void redefine(TypeVariable name, ConcreteType express) {
		if (!pairs.containsKey(name)) DNEErrors.variableDNE(name);
		pairs.put(name, express);
	}
}
