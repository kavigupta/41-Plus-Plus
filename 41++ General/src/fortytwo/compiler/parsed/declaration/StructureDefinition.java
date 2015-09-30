package fortytwo.compiler.parsed.declaration;

import java.util.HashMap;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.Sentence;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.constructions.GenericStructureSignature;
import fortytwo.vm.environment.type.AbstractTypeEnvironment;
import fortytwo.vm.expressions.LiteralFunction;

/**
 * A class representing structure definition.
 */
public class StructureDefinition implements Sentence {
	/**
	 * The structure declared here
	 */
	private final GenericStructureSignature structure;
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
	/**
	 * Registers this structure on the given type environment and registers the
	 * accessor functions on the given function roster.
	 * 
	 * @param environment
	 *        the type environment to register this structure on
	 * @param functions
	 *        the function roster to register this function's accessors on
	 */
	public void register(AbstractTypeEnvironment environment,
			HashMap<VariableIdentifier, LiteralFunction> functions) {
		environment.typeRoster.structs.addStructure(structure);
		functions.putAll(structure.fieldFunctions());
	}
	@Override
	public SentenceType kind() {
		return SentenceType.DECLARATION_STRUCT;
	}
	@Override
	public String toSourceCode() {
		String fields = SourceCode.displayFieldList(structure.fields);
		if (fields.length() != 0) fields = " that contains " + fields;
		return "Define a type called " + structure.type.toSourceCode() + fields;
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
