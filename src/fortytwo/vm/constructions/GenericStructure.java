package fortytwo.vm.constructions;

import java.util.List;

import fortytwo.language.field.GenericField;
import fortytwo.language.type.GenericStructureType;

public class GenericStructure {
	public final GenericStructureType type;
	public final List<GenericField> fields;
	public GenericStructure(GenericStructureType type,
			List<GenericField> fields) {
		this.type = type;
		this.fields = fields;
	}
}