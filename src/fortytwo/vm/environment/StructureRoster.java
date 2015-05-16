package fortytwo.vm.environment;

import java.util.ArrayList;
import java.util.List;

import fortytwo.compiler.language.identifier.TypeIdentifier;
import fortytwo.vm.constructions.GenericStructure;
import fortytwo.vm.constructions.Structure;

public class StructureRoster {
	public List<GenericStructure> roster;
	public StructureRoster() {
		this.roster = new ArrayList<>();
	}
	public void addStructure(GenericStructure s) {
		roster.add(s);
	}
	public Structure referenceTo(TypeIdentifier type) {
		// TODO Auto-generated method stub
		return null;
	}
}
