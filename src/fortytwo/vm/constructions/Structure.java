package fortytwo.vm.constructions;

import java.util.List;

import fortytwo.language.field.Field;
import fortytwo.language.type.StructureType;

public class Structure {
	public final StructureType type;
	public final List<Field> fields;
	public Structure(StructureType type, List<Field> fields) {
		this.type = type;
		this.fields = fields;
	}
}
