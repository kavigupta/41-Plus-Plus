package fortytwo.library.standard;

import java.util.Arrays;
import java.util.List;

import lib.standard.collections.Pair;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.language.Language;
import fortytwo.language.field.GenericField;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.identifier.functioncomponent.FunctionArgument;
import fortytwo.language.identifier.functioncomponent.FunctionComponent;
import fortytwo.language.identifier.functioncomponent.FunctionToken;
import fortytwo.language.type.GenericStructureType;
import fortytwo.language.type.TypeVariable;
import fortytwo.vm.constructions.GenericStructure;
import fortytwo.vm.environment.FunctionRoster;
import fortytwo.vm.environment.StructureRoster;

public class StdLib42 {
	// TODO rest
	public static final StructureRoster DEF_STRUCT = new StructureRoster();
	public static final FunctionRoster DEF_FUNC = new FunctionRoster();
	public static final FunctionName FUNC_FIELD_ACCESS_NAME_APPARENT = FunctionName
			.getInstance(Arrays.asList(new FunctionToken("the"),
					FunctionArgument.INSTANCE, new FunctionToken("of"),
					FunctionArgument.INSTANCE));
	static {
		addPair();
	}
	private static void addPair() {
		TypeVariable _k = new TypeVariable(
				VariableIdentifier.getInstance("_k"));
		TypeVariable _v = new TypeVariable(
				VariableIdentifier.getInstance("_v"));
		DEF_STRUCT.addStructure(new GenericStructure(
				new GenericStructureType(Arrays.asList("a", "pair"), Arrays
						.asList(_k, _v)), Arrays.asList(
						new GenericField(VariableIdentifier
								.getInstance("key"), _k),
						new GenericField(VariableIdentifier
								.getInstance("value"), _v))));
	}
	public static Pair<FunctionName, List<ParsedExpression>> parse(
			List<FunctionComponent> name, List<ParsedExpression> arguments) {
		if (name.equals(StdLib42.FUNC_FIELD_ACCESS_NAME_APPARENT)) {
			if (!(arguments.get(0) instanceof VariableIdentifier))
				throw new RuntimeException(/* LOWPRI-E */);
			return Pair
					.getInstance(
							getFieldAccess(((VariableIdentifier) arguments
									.get(0)).name), Arrays
									.asList(arguments.get(1)));
		}
		return Pair.getInstance(FunctionName.getInstance(name), arguments);
	}
	public static FunctionName getFieldAccess(String name) {
		return FunctionName.getInstance(Arrays.asList(
				new FunctionToken("the"), new FunctionToken(name),
				new FunctionToken("of"), FunctionArgument.INSTANCE));
	}
	public static boolean matchesFieldAccess(FunctionName name) {
		if (name.function.size() != 4) return false;
		if (!name.function.get(0).equals("the")) return false;
		if (!(name.function.get(1) instanceof FunctionToken)
				&& Language
						.isValidVariableIdentifier(((FunctionToken) name.function
								.get(1)).token)) return false;
		if (!name.function.get(2).equals("of")) return false;
		if (!(name.function.get(3) instanceof FunctionArgument))
			return false;
		return true;
	}
}
