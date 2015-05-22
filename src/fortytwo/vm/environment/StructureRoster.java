package fortytwo.vm.environment;

import java.util.ArrayList;
import java.util.List;

import fortytwo.language.field.Field;
import fortytwo.language.field.GenericField;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.*;
import fortytwo.vm.constructions.GenericStructure;
import fortytwo.vm.constructions.Structure;
import fortytwo.vm.expressions.*;

public class StructureRoster {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((structs == null) ? 0 : structs.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		StructureRoster other = (StructureRoster) obj;
		if (structs == null) {
			if (other.structs != null) return false;
		} else if (!structs.equals(other.structs)) return false;
		return true;
	}
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
		if (type instanceof ArrayType) {
			if (fieldValues.pairs.size() != 1)
				throw new RuntimeException(/* LOWPRI-E */);
			if (!fieldValues.pairs.containsKey(VariableIdentifier
					.getInstance("_length")))
				throw new RuntimeException(/* LOWPRI-E */);
			LiteralExpression length = fieldValues.pairs
					.get(VariableIdentifier.getInstance("_length"));
			if (length.resolveType() != PrimitiveType.NUMBER)
				throw new RuntimeException(/* LOWPRI-E */);
			return new LiteralArray(((ArrayType) type).contentType,
					((LiteralNumber) length).contents.intValue());
		}
		if (!(type instanceof StructureType))
			throw new RuntimeException(/* LOWPRI-E */);
		return new LiteralObject(getStructure((StructureType) type),
				fieldValues);
	}
	public Structure getStructure(StructureType struct) {
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
		if (fields.value() != null) {
			if (fields.value().resolveType().equals(type)) return true;
			throw new RuntimeException(/* LOWPRI-E */);
		}
		if (type instanceof ArrayType) {
			if (fields.pairs.size() != 1) throw new RuntimeException(/*
														 * LOWPRI-E
														 */);
			Expression length = fields.pairs.get(VariableIdentifier
					.getInstance("_length"));
			if (length == null
					|| length.resolveType() != PrimitiveType.NUMBER)
				throw new RuntimeException(/* LOWPRI-E */);
			return true;
		}
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
