package fortytwo.vm.environment;

import java.util.HashMap;

import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.TypeVariable;

public class TypeVariableRoster {
	public final HashMap<TypeVariable, ConcreteType> pairs = new HashMap<>();
	public void assign(TypeVariable name, ConcreteType express) {
		if (pairs.containsKey(name))
			throw new RuntimeException(/* LOWPRI-E */);
		pairs.put(name, express);
	}
	public ConcreteType referenceTo(TypeVariable id) {
		ConcreteType le = pairs.get(id);
		if (le == null) throw new RuntimeException(/* LOWPRI-E */);
		return le;
	}
	public void redefine(TypeVariable name, ConcreteType express) {
		if (!pairs.containsKey(name))
			throw new RuntimeException(/* LOWPRI-E */);
		pairs.put(name, express);
	}
}
