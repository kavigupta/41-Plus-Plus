package fortytwo.compiler.language.type;

import fortytwo.compiler.language.identifier.VariableIdentifier;

public class GenericField {
	public final VariableIdentifier name;
	public final GenericType type;
	public GenericField(VariableIdentifier name, GenericType type) {
		this.name = name;
		this.type = type;
	}
}
