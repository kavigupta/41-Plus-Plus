package fortytwo.vm.environment;

import java.util.ArrayList;
import java.util.List;

import fortytwo.compiler.language.identifier.TypeIdentifier;
import fortytwo.vm.constructions.Structure;

public class StructureRoster {
	public List<Structure> roster;
	public StructureRoster() {
		this.roster = new ArrayList<>();
	}
	public void addStructure(Structure s) {
		roster.add(s);
	}
	public Structure referenceTo(TypeIdentifier type) {
		// TODO Auto-generated method stub
		return null;
	}
}
