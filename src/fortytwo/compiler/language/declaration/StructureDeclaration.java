package fortytwo.compiler.language.declaration;

import fortytwo.vm.constructions.GenericStructure;

public class StructureDeclaration implements Declaration {
	public final GenericStructure structure;
	public StructureDeclaration(GenericStructure structure) {
		this.structure = structure;
	}
	@Override
	public SentenceType type() {
		return SentenceType.DECLARATION_STRUCT;
	}
}
