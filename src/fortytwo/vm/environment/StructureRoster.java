package fortytwo.vm.environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.language.field.Field;
import fortytwo.language.field.GenericField;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.*;
import fortytwo.vm.constructions.GenericStructure;
import fortytwo.vm.constructions.Structure;
import fortytwo.vm.errors.DNEErrors;
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
		DNEErrors.structureDNE(type, this);
		// should not be reachable.
		return null;
	}
	public Field typeOf(ConcreteType type, VariableIdentifier field) {
		if (!(type instanceof StructureType))
			DNEErrors.fieldAccessOnPrimitive(type, Arrays.asList(field));
		Structure struct = getStructure((StructureType) type);
		for (Field f : struct.fields) {
			if (f.name.equals(field)) { return f; }
		}
		DNEErrors.fieldDNE(struct, field);
		// should not be reachable.
		return null;
	}
	public LiteralExpression instance(Field name,
			VariableRoster<LiteralExpression> fieldValues, Context context) {
		LiteralExpression value = fieldValues.value();
		if (value != null) return value;
		typeCheckConstructor(name, fieldValues, context);
		if (name.type instanceof StructureType)
			return new LiteralObject(
					getStructure((StructureType) name.type), fieldValues,
					Context.synthetic());
		return new LiteralArray(((ArrayType) name.type).contentType,
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
		DNEErrors.structureDNE(genericType, this);
		// should never get here
		return null;
	}
	private GenericStructureType genericVersionOf(StructureType type) {
		for (GenericStructure gs : structs)
			if (gs.type.name.equals(type.name)) return gs.type;
		DNEErrors.structureDNE(type, this);
		// should never happen
		return null;
	}
	public boolean typeCheckConstructor(Field name, VariableRoster<?> fields,
			Context context) {
		if (fields.value() != null) {
			if (fields.value().resolveType().equals(name.type)) return true;
			TypingErrors.redefinitionTypeMismatch(name, fields.value());
		}
		if (name.type instanceof ArrayType) {
			Expression length = fields.referenceTo(TypeVariable.LENGTH.name);
			if (fields.size() == 1
					&& length != null
					&& length.resolveType().equals(
							new PrimitiveType(PrimitiveTypes.NUMBER,
									Context.synthetic())))
				return true;
			if (fields.size() == 0)
				TypingErrors.incompleteArrayConstructor(context);
			else DNEErrors.fieldDNEInArray(fields.variables().get(0));
		}
		if (!(name.type instanceof StructureType)) {
			// nothing but a structure or array has anything but a value
			DNEErrors.fieldAccessOnPrimitive(name.type, fields.variables());
		}
		Structure struct = getStructure((StructureType) name.type);
		for (Field f : struct.fields) {
			if (!fields.referenceTo(f.name).resolveType().equals(f.type))
				TypingErrors.fieldAssignmentTypeMismatch(struct, f, f);
		}
		fields.forEach(x -> {
			if (!struct.containsField(x.key))
				DNEErrors.fieldDNE(struct, x.key);
		});
		struct.fields
				.stream()
				.filter(f -> !fields.assigned(f.name))
				.forEach(f -> TypingErrors.incompleteConstructor(struct,
						fields));
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
