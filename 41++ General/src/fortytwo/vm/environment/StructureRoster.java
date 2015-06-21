package fortytwo.vm.environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.language.field.Field;
import fortytwo.language.field.GenericField;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.*;
import fortytwo.vm.constructions.GenericStructureSignature;
import fortytwo.vm.constructions.Structure;
import fortytwo.vm.constructions.VariableRoster;
import fortytwo.vm.errors.DNEErrors;
import fortytwo.vm.errors.TypingErrors;
import fortytwo.vm.expressions.LiteralArray;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralNumber;
import fortytwo.vm.expressions.LiteralObject;

public class StructureRoster {
	public List<GenericStructureSignature> structs;
	public StructureRoster() {
		this.structs = new ArrayList<>();
	}
	public void addStructure(GenericStructureSignature structure) {
		structs.add(structure);
	}
	public GenericStructureSignature referenceTo(GenericStructureType type) {
		for (GenericStructureSignature struct : structs) {
			if (struct.type.equals(type)) return struct;
		}
		DNEErrors.typeDNE(type);
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
		typeCheckConstructor(null, name, fieldValues, context);
		if (name.type instanceof StructureType)
			return new LiteralObject(
					getStructure((StructureType) name.type), fieldValues,
					Context.SYNTHETIC);
		return new LiteralArray(((ArrayType) name.type).contentType,
				((LiteralNumber) fieldValues
						.referenceTo(TypeVariable.LENGTH.name)).contents
						.intValue(), Context.SYNTHETIC);
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
	private GenericStructureSignature getGenericStructure(
			GenericStructureType genericType) {
		for (GenericStructureSignature gs : structs)
			if (gs.type.equals(genericType)) return gs;
		DNEErrors.typeDNE(genericType);
		// should never get here
		return null;
	}
	private GenericStructureType genericVersionOf(StructureType type) {
		for (GenericStructureSignature gs : structs)
			if (gs.type.name.equals(type.name)) return gs.type;
		DNEErrors.typeDNE(type);
		// should never happen
		return null;
	}
	public boolean typeCheckConstructor(StaticEnvironment env, Field name,
			VariableRoster<? extends Expression> fieldValues, Context context) {
		if (fieldValues.value() != null) {
			if (fieldValues.value().type(env).equals(name.type))
				return true;
			TypingErrors.redefinitionTypeMismatch(name, fieldValues.value(),
					env);
		}
		if (name.type instanceof ArrayType) {
			Expression length = fieldValues
					.referenceTo(TypeVariable.LENGTH.name);
			if (fieldValues.numberOfVariables() == 1
					&& length != null
					&& length.type(env)
							.equals(new PrimitiveType(
									PrimitiveTypeWithoutContext.NUMBER,
									Context.SYNTHETIC))) return true;
			if (fieldValues.numberOfVariables() == 0)
				TypingErrors.incompleteArrayConstructor(context);
			else DNEErrors.fieldDNEInArray(fieldValues.pairs.get(0).key);
		}
		if (!(name.type instanceof StructureType)) {
			if (fieldValues.numberOfVariables() == 0)
				TypingErrors.noValue(name);
			// nothing but a structure or array has anything but a value
			DNEErrors.fieldAccessOnPrimitive(name.type, fieldValues.pairs
					.stream().map(x -> x.key).collect(Collectors.toList()));
		}
		Structure struct = getStructure((StructureType) name.type);
		for (Field f : struct.fields) {
			if (!fieldValues.referenceTo(f.name).type(env).equals(f.type))
				TypingErrors.fieldAssignmentTypeMismatch(struct, f,
						fieldValues.referenceTo(f.name), env);
		}
		fieldValues.pairs.forEach(x -> {
			if (!struct.containsField(x.key))
				DNEErrors.fieldDNE(struct, x.key);
		});
		struct.fields
				.stream()
				.filter(f -> !fieldValues.assigned(f.name))
				.forEach(f -> TypingErrors.incompleteConstructor(struct,
						fieldValues));
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
