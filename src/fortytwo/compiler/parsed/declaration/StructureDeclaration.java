package fortytwo.compiler.parsed.declaration;

import fortytwo.compiler.parser.GenericStructure;

public class StructureDeclaration implements Declaration {
	public final GenericStructure structure;
	// It may seem strange that a structure declaration would take a generic
	// structure as an argument. However, a declaration is by definition
	// generic. What can't be generic is an object definition
	public StructureDeclaration(GenericStructure structure) {
		this.structure = structure;
	}
	@Override
	public SentenceType type() {
		return SentenceType.DECLARATION_STRUCT;
	}
}
