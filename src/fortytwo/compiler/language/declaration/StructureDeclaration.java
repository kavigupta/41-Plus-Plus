package fortytwo.compiler.language.declaration;

import fortytwo.vm.constructions.Structure;

public class StructureDeclaration implements Declaration {
	public final Structure structure;
	public StructureDeclaration(Structure structure) {
		this.structure = structure;
	}
	@Override
	public SentenceType type() {
		return SentenceType.DECLARATION_STRUCT;
	}
}
