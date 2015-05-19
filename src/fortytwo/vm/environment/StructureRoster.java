package fortytwo.vm.environment;

import java.util.ArrayList;
import java.util.List;

import fortytwo.language.field.Field;
import fortytwo.language.field.GenericField;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.GenericStructureType;
import fortytwo.language.type.StructureType;
import fortytwo.vm.constructions.GenericStructure;
import fortytwo.vm.constructions.Structure;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralObject;

// TODO handle arrays
public class StructureRoster {
	public List<GenericStructure> structs;
	public StructureRoster() {
		this.structs = new ArrayList<>();
	}
	public void addStructure(GenericStructure structure) {
		structs.add(structure);
	}
	public GenericStructure referenceTo(GenericStructureType type) {
		for (GenericStructure struct : structs) {
			if (struct.type.equals(type)) return struct;
		}
		throw new RuntimeException(/* LOWPRI-E */);
	}
	public Field typeOf(ConcreteType type, VariableIdentifier field) {
		if (!(type instanceof StructureType))
			throw new RuntimeException(/* LOWPRI-E */);
		for (Field f : getStructure((StructureType) type).fields) {
			if (f.name.equals(field)) { return f; }
		}
		throw new RuntimeException(/* LOWPRI-E */);
	}
	public LiteralExpression instance(ConcreteType type,
			LiteralVariableRoster fieldValues) {
		LiteralExpression exp = fieldValues.value();
		if (exp != null) return exp;
		if (!(type instanceof StructureType))
			throw new RuntimeException(/* LOWPRI-E */);
		return new LiteralObject(getStructure((StructureType) type),
				fieldValues);
	}
	public Structure getStructure(StructureType type) {
		StructureType struct = type;
		GenericStructureType genericType = genericVersionOf(struct);
		GenericStructure baseStructure = getStructure(genericType);
		List<GenericField> fieldsGeneric = baseStructure.fields;
		List<Field> fields = new ArrayList<>();
		for (int i = 0; i < struct.types.size(); i++) {
			for (GenericField f : fieldsGeneric) {
				if (f.equals(genericType.typeVariables.get(i))) {
					fields.add(new Field(f.name, struct.types.get(i)));
				}
			}
		}
		Structure structure = new Structure(struct, fields);
		return structure;
	}
	private GenericStructure getStructure(GenericStructureType genericType) {
		for (GenericStructure gs : structs)
			if (gs.type.equals(genericType)) return gs;
		throw new RuntimeException(/* LOWPRI-E */);
	}
	private GenericStructureType genericVersionOf(StructureType type) {
		for (GenericStructure gs : structs)
			if (gs.type.name.equals(type.name)) return gs.type;
		throw new RuntimeException(/* LOWPRI-E */);
	}
	public boolean typeCheckConstructor(ConcreteType type,
			VariableRoster fields) {
		if (!(type instanceof StructureType)) {
			if (fields.value().resolveType().equals(type)) return true;
			throw new RuntimeException(/* LOWPRI-E */);
		}
		Structure struct = getStructure((StructureType) type);
		for (Field f : struct.fields) {
			if (!fields.referenceTo(f.name).resolveType().equals(f.type))
				throw new RuntimeException(/* LOWPRI-E */);
		}
		return true;
	}
	@Override
	public StructureRoster clone() {
		StructureRoster other = new StructureRoster();
		other.structs.addAll(structs);
		return other;
	}
}
