package fortytwo.vm.environment;

import java.util.ArrayList;
import java.util.List;

import fortytwo.language.field.Field;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.GenericStructureType;
import fortytwo.language.type.Structure;

public class StructureRoster {
	public List<GenericStructureType> roster;
	public StructureRoster() {
		this.roster = new ArrayList<>();
	}
	public void addStructure(GenericStructureType s) {
		roster.add(s);
	}
	public Structure referenceTo(ConcreteType type) {
		// TODO Auto-generated method stub
		return null;
	}
	public Field typeOf(VariableIdentifier name, VariableIdentifier field) {
		// TODO Auto-generated method stub
		return null;
	}
}
