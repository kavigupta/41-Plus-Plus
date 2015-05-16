package fortytwo.language.field;

import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.GenericType;

public class GenericField {
	public final VariableIdentifier name;
	public final GenericType type;
	public GenericField(VariableIdentifier name, GenericType type) {
		this.name = name;
		this.type = type;
	}
}
