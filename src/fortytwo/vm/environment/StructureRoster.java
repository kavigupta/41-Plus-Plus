package fortytwo.vm.environment;

import java.util.ArrayList;
import java.util.List;

import lib.standard.collections.Pair;
import fortytwo.language.field.Field;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.GenericStructureType;
import fortytwo.language.type.StructureType;
import fortytwo.language.type.TypeVariable;
import fortytwo.vm.constructions.GenericStructure;
import fortytwo.vm.constructions.Structure;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralObject;

public class StructureRoster {
	public List<GenericStructure> roster;
	public StructureRoster() {
		this.roster = new ArrayList<>();
	}
	public void addStructure(GenericStructure structure) {
		roster.add(structure);
	}
	public GenericStructure referenceTo(GenericStructureType type) {
		for (GenericStructure struct : roster) {
			if (struct.type.equals(type)) return struct;
		}
		throw new RuntimeException(/* LOWPRI-E */);
	}
	public Field typeOf(VariableIdentifier name, VariableIdentifier field) {
		// TODO Auto-generated method stub
		return null;
	}
	public LiteralExpression instance(ConcreteType type,
			VariableRoster fieldValues) {
		LiteralExpression exp = fieldValues.value();
		if (exp != null) return exp;
		if (!(type instanceof StructureType))
			throw new RuntimeException(/* LOWPRI-E */);
		StructureType struct = (StructureType) type;
		GenericStructureType genericType = genericVersionOf(struct);
		List<Pair<TypeVariable, ConcreteType>> typeVariables = new ArrayList<>();
		if (struct.types.size() != genericType.typeVariables.size())
			throw new RuntimeException(/* LOWPRI-E */);
		for (int i = 0; i < struct.types.size(); i++) {
			typeVariables
					.add(Pair.getInstance(
							genericType.typeVariables.get(i),
							struct.types.get(i)));
		}
		Structure structure = new Structure(struct, typeVariables);
		return new LiteralObject(structure, fieldValues);
	}
	private GenericStructureType genericVersionOf(StructureType type) {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean typeCheckConstructor(ConcreteType type,
			VariableRoster fields) {
		// TODO Auto-generated method stub
		return false;
	}
}
