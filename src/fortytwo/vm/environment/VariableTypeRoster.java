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
		if (type == null) throw new RuntimeException(/* LOWPRI-E */);
		return type;
	}
	@Override
	public VariableTypeRoster clone() {
		VariableTypeRoster other = new VariableTypeRoster();
		other.pairs.putAll(pairs);
		return other;
	}
}
