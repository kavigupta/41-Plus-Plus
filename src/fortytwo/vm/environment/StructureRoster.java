package fortytwo.vm.environment;

import java.util.ArrayList;
import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.language.field.Field;
import fortytwo.language.field.GenericField;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.*;
import fortytwo.vm.constructions.GenericStructure;
import fortytwo.vm.constructions.Structure;
import fortytwo.vm.errors.CompilerErrors;
import fortytwo.vm.errors.TypingErrors;
import fortytwo.vm.expressions.*;

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
		TypingErrors.structureNotFound(type, this);
		// should not be reachable.
		return null;
	}
	public Field typeOf(ConcreteType type, VariableIdentifier field) {
		if (!(type instanceof StructureType))
			TypingErrors.fieldAccessCalledOnPrimitive(type, field);
		for (Field f : getStructure((StructureType) type).fields) {
			if (f.name.equals(field)) { return f; }
		}
		TypingErrors.fieldNotFound(type, field, this);
		// should not be reachable.
		return null;
	}
	public LiteralExpression instance(ConcreteType type,
			VariableRoster<LiteralExpression> fieldValues) {
		LiteralExpression value = fieldValues.value();
		if (value != null) return value;
		typeCheckConstructor(type, fieldValues);
		if (type instanceof StructureType)
			return new LiteralObject(getStructure((StructureType) type),
					fieldValues, Context.synthetic());
		return new LiteralArray(((ArrayType) type).contentType,
				((LiteralNumber) fieldValues
						.referenceTo(TypeVariable.LENGTH.name)).contents
						.intValue(), Context.synthetic());
	}
	public Structure getStructure(StructureType struct) {
		GenericStructureType genericType = genericVersionOf(struct);
		List<ConcreteType> typeParameters = new ArrayList<>(struct.types);
		List<Field> fields = new ArrayList<>();
		for (GenericField f : getGenericStructure(genericType).fields) {
			if (f.type instanceof ConcreteType) {
				fields.add(new Field(f.name, (ConcreteType) f.type));
				continue;
			}
			fields.add(new Field(f.name, typeParameters.remove(0)));
		}
		return new Structure(struct, fields);
	}
	private GenericStructure getGenericStructure(
			GenericStructureType genericType) {
		for (GenericStructure gs : structs)
			if (gs.type.equals(genericType)) return gs;
		CompilerErrors.structureDNE(genericType, this);
		// should never get here
		return null;
	}
	private GenericStructureType genericVersionOf(StructureType type) {
		for (GenericStructure gs : structs)
			if (gs.type.name.equals(type.name)) return gs.type;
		CompilerErrors.structureDNE(type, this);
		// should never happen
		return null;
	}
	public boolean typeCheckConstructor(ConcreteType type,
			VariableRoster<?> fields) {
		if (fields.value() != null) {
			if (fields.value().resolveType().equals(type)) return true;
			TypingErrors.fieldAccessCalledOnPrimitive(type, fields);
		}
		if (type instanceof ArrayType) {
			Expression length = fields.referenceTo(TypeVariable.LENGTH.name);
			if (fields.size() == 1 && length != null
					&& length.resolveType() == PrimitiveType.NUMBER)
				return true;
			TypingErrors.nonLengthFieldAccessOnArray((ArrayType) type,
					fields);
		}
		if (!(type instanceof StructureType)) {
			// nothing but a structure or array has anything but a value
			TypingErrors.fieldAccessCalledOnPrimitive(type, fields);
		}
		Structure struct = getStructure((StructureType) type);
		for (Field f : struct.fields) {
			if (!fields.referenceTo(f.name).resolveType().equals(f.type))
				TypingErrors.fieldAssignmentTypeMismatch(struct, f, f.type);
		}
		fields.forEach(x -> {
			if (!struct.containsField(x.key))
				TypingErrors.fieldNotFound(type, x.key, this);
		});
		struct.fields
				.stream()
				.filter(f -> !fields.assigned(f.name))
				.forEach(f -> TypingErrors.fieldAssignmentIncomplete(
						struct, fields));
		return true;
	}
	@Override
	public StructureRoster clone() {
		StructureRoster other = new StructureRoster();
		other.structs.addAll(structs);
		return other;
	}
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
}
