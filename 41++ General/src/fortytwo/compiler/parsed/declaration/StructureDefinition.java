package fortytwo.compiler.parsed.declaration;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.Sentence;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.vm.constructions.GenericStructureSignature;

/**
 * A class representing structure definition.
 */
public class StructureDefinition implements Sentence {
	/**
	 * The structure declared here
	 */
	public final GenericStructureSignature structure;
	private final Context context;
	/**
	 * @param structure
	 *        the structure to be used. It may seem strange that a
	 *        structure declaration would take a generic structure as an
	 *        argument. However, a declaration is by definition generic. What
	 *        can't be generic is an object definition
	 * @param context
	 *        the context of the declaration
	 */
	public StructureDefinition(GenericStructureSignature structure,
			Context context) {
		this.structure = structure;
		this.context = context;
	}
	@Override
	public SentenceType kind() {
		return SentenceType.DECLARATION_STRUCT;
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this);
	}
	@Override
	public boolean isSimple() {
		return true;
	}
	@Override
	public Context context() {
		return context;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (structure == null ? 0 : structure.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final StructureDefinition other = (StructureDefinition) obj;
		if (structure == null) {
			if (other.structure != null) return false;
		} else if (!structure.equals(other.structure)) return false;
		return true;
	}
}
