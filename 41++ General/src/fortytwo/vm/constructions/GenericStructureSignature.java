package fortytwo.vm.constructions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fortytwo.language.field.GenericField;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.FunctionType;
import fortytwo.language.type.GenericStructureType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.library.standard.StdLibImplementations;
import fortytwo.vm.expressions.LiteralFunction;

public class GenericStructureSignature {
	public final GenericStructureType type;
	public final List<GenericField> fields;
	public GenericStructureSignature(GenericStructureType type,
			List<GenericField> fields) {
		this.type = type;
		this.fields = fields;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (fields == null ? 0 : fields.hashCode());
		result = prime * result + (type == null ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final GenericStructureSignature other = (GenericStructureSignature) obj;
		if (fields == null) {
			if (other.fields != null) return false;
		} else if (!fields.equals(other.fields)) return false;
		if (type == null) {
			if (other.type != null) return false;
		} else if (!type.equals(other.type)) return false;
		return true;
	}
	@Override
	public String toString() {
		return "GenericStructure [type=" + type + ", fields=" + fields + "]";
	}
	public Map<VariableIdentifier, LiteralFunction> fieldFunctions() {
		Map<VariableIdentifier, LiteralFunction> fieldFuncs = new HashMap<>();
		for (GenericField field : fields) {
			FunctionName accessorName = FunctionName.getInstance("the",
					field.name.toSourceCode(), "of", "");
			FunctionName modifierName = FunctionName.getInstance("Set", "the",
					field.name.toSourceCode(), "of", "", "to", "");
			FunctionType accessorType = new FunctionType(
					Arrays.asList(this.type), field.type);
			FunctionType modifierType = new FunctionType(
					Arrays.asList(this.type, field.type),
					PrimitiveType.SYNTH_VOID);
			fieldFuncs.put(
					new FunctionSignature(accessorName, accessorType)
							.identifier(),
					new FunctionSynthetic(accessorType,
							StdLibImplementations.fieldAccess(field.name)));
			fieldFuncs.put(
					new FunctionSignature(modifierName, modifierType)
							.identifier(),
					new FunctionSynthetic(modifierType, StdLibImplementations
							.fieldModification(field.name)));
		}
		return fieldFuncs;
	}
}
