package fortytwo.compiler.language.declaration;

import fortytwo.vm.constructions.GenericStructure;

public class StructureDeclaration implements Declaration {
	public final GenericStructure structure;
	// It may seem strange that a structure declaration would take a generic
	// structure as an argument. However, a declaration is definitionally
	// generic.
	public StructureDeclaration(GenericStructure structure) {
		this.structure = structure;
	}
	@Override
	public SentenceType type() {
		return SentenceType.DECLARATION_STRUCT;
	}
}
