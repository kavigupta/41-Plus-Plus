package fortytwo.vm.constructions;

import fortytwo.compiler.language.identifier.TypeIdentifier;
import fortytwo.compiler.language.identifier.VariableIdentifier;

public class Field {
	public final VariableIdentifier name;
	public final TypeIdentifier type;
	public Field(VariableIdentifier name, TypeIdentifier type) {
		this.name = name;
		this.type = type;
	}
}
