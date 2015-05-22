package fortytwo.vm.environment;

import java.util.HashMap;

import fortytwo.language.field.Field;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;

public class VariableTypeRoster {
	public final HashMap<VariableIdentifier, ConcreteType> pairs = new HashMap<>();
	public void add(VariableIdentifier variableIdentifier,
			ConcreteType expression) {
		pairs.put(variableIdentifier, expression);
	}
	public void add(Field field) {
		add(field.name, field.type);
	}
	public ConcreteType typeOf(VariableIdentifier name) {
		ConcreteType type = pairs.get(name);
		if (type == null)
			throw new RuntimeException(/* LOWPRI-E */name.toSourceCode()
					+ "\t" + pairs);
		return type;
	}
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
		VariableTypeRoster other = (VariableTypeRoster) obj;
		if (pairs == null) {
			if (other.pairs != null) return false;
		} else if (!pairs.equals(other.pairs)) return false;
		return true;
	}
	@Override
	public VariableTypeRoster clone() {
		VariableTypeRoster other = new VariableTypeRoster();
		other.pairs.putAll(pairs);
		return other;
	}
	@Override
	public String toString() {
		return "VariableTypeRoster [pairs=" + pairs + "]";
	}
}
