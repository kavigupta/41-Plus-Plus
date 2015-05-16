package fortytwo.compiler.language.type;

import fortytwo.compiler.language.identifier.VariableIdentifier;

public class Field {
	public final VariableIdentifier name;
	public final ConcreteType type;
	public Field(VariableIdentifier name, ConcreteType type) {
		this.name = name;
		this.type = type;
	}
}
