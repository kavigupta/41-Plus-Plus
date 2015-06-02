package fortytwo.vm.errors;

import java.util.List;

import fortytwo.language.ParsedConstruct;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.GenericStructureType;
import fortytwo.language.type.StructureType;
import fortytwo.language.type.TypeVariable;
import fortytwo.vm.constructions.Structure;
import fortytwo.vm.environment.FunctionSignatureRoster;
import fortytwo.vm.environment.StructureRoster;

public class DNEErrors {
	public static void dneError(String description, ParsedConstruct name) {
		// The <description> ~<name>~ does not exist here
		Errors.error(ErrorType.DNE, String.format(
				"The %s ~%s~ does not exist here", description,
				name.toSourceCode()), name.context());
	}
	public static void variableDNE(TypeVariable id) {
		// TODO 0 implement
		dneError("type variable", id);
	}
	public static void variableDNE(VariableIdentifier name) {
		// TODO Auto-generated method stub
	}
	public static void structureDNE(StructureType type,
			StructureRoster structureRoster) {
		// TODO Auto-generated method stub
	}
	public static void structureDNE(GenericStructureType genericType,
			StructureRoster structureRoster) {
		// TODO Auto-generated method stub
	}
	public static void functionSignatureDNE(FunctionName name,
			List<ConcreteType> types, FunctionSignatureRoster funcs) {
		System.out.println("FunctionSig DNE: " + name + "\t" + funcs.funcs);
		// TODO Auto-generated method stub
	}
	public static void fieldDNE(Structure struct, VariableIdentifier key) {
		// TODO Auto-generated method stub
	}
	public static void fieldDNEInArray(VariableIdentifier field) {
		// TODO 0 implement
	}
	public static void fieldAccessOnPrimitive(ConcreteType type,
			List<VariableIdentifier> field) {
		// TODO 0 implement
	}
}
