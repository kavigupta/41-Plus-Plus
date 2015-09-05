package fortytwo.vm.errors;

import java.util.ArrayList;
import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.GenericType;
import fortytwo.vm.VirtualMachine;
import fortytwo.vm.constructions.Structure;

public class DNEErrors {
	public static void dneError(String description, String name, String suffix,
			Context context) {
		// The <description> ~<name>~ does not exist here
		VirtualMachine.error(ErrorType.DNE,
				String.format("The %s ~%s~ does not exist%s", description, name,
						suffix),
				context);
	}
	public static void typeDNE(GenericType id) {
		dneError("type", id.toSourceCode(), " here", id.context());
	}
	public static void variableDNE(VariableIdentifier name) {
		dneError("variable", name.toSourceCode(), " here", name.context());
	}
	public static void functionSignatureDNE(FunctionName name,
			List<ConcreteType> types) {
		if (name.function.size() == 0)
			throw new RuntimeException("Zero length function??????");
		final ArrayList<Context> context = new ArrayList<>();
		context.add(name.context());
		types.forEach(x -> context.add(x.context()));
		dneError("function", name.display(types), " here",
				Context.sum(context));
	}
	public static void fieldDNE(Structure struct, VariableIdentifier field) {
		dneError("field", "the " + field.toSourceCode() + " of "
				+ struct.type.toSourceCode(), " here", field.context());
	}
	public static void fieldDNEInArray(VariableIdentifier field) {
		dneError("field", "the " + field.toSourceCode() + " of an array",
				"; only \"length\" exists", field.context());
	}
	public static void fieldAccessOnPrimitive(ConcreteType type,
			List<VariableIdentifier> field) {
		dneError("field",
				"the " + field.get(0).toSourceCode() + " of "
						+ type.toSourceCode(),
				"; no fields exist on primitives", field.get(0).context());
	}
}
