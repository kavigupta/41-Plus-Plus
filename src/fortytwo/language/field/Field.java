package fortytwo.language.field;

import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;

public class Field {
	public final VariableIdentifier name;
	public final ConcreteType type;
	public Field(VariableIdentifier name, ConcreteType type) {
		this.name = name;
		this.type = type;
	}
}
