package fortytwo.vm.constructions;

import java.util.List;

import lib.standard.collections.Pair;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.StructureType;
import fortytwo.language.type.TypeVariable;

public class Structure {
	public final StructureType type;
	public final List<Pair<TypeVariable, ConcreteType>> fields;
	public Structure(StructureType type,
			List<Pair<TypeVariable, ConcreteType>> fields) {
		this.type = type;
		this.fields = fields;
	}
}
