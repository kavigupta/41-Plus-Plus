package fortytwo.vm.constructions;

import java.util.ArrayList;
import java.util.List;

import fortytwo.compiler.language.identifier.TypeIdentifier;

public class GenericStructure {
	public final List<String> name;
	public final List<TypeIdentifier> typeVariables;
	public final List<Field> fields;
	public GenericStructure(List<String> name,
			List<TypeIdentifier> typeVariables, ArrayList<Field> fields) {
		this.name = name;
		this.typeVariables = typeVariables;
		this.fields = fields;
	}
}
