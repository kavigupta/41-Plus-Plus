package fortytwo.library.standard;

import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;

import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.language.Resources;
import fortytwo.language.field.GenericField;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.identifier.functioncomponent.FunctionComponent;
import fortytwo.language.type.GenericStructureType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.TypeVariable;
import fortytwo.vm.constructions.FunctionSynthetic;
import fortytwo.vm.constructions.GenericStructureSignature;
import fortytwo.vm.environment.FunctionRoster;
import fortytwo.vm.environment.FunctionSignatureRoster;
import fortytwo.vm.environment.StructureRoster;
import fortytwo.vm.errors.ParserErrors;
import fortytwo.vm.expressions.LiteralFunction;
import fortytwo.vm.expressions.LiteralFunction.FunctionImplementation;

public class StdLib42 {
	public static final StructureRoster DEF_STRUCT = new StructureRoster();
	public static final FunctionName FUNC_FIELD_ACCESS_NAME_APPARENT = FunctionName
			.getInstance("the", "", "of", "");
	public static final FunctionName FUNC_FIELD_MODIFICATION_NAME_APPARENT = FunctionName
			.getInstance("Set", "the", "", "of", "", "to", "");
	public static final FunctionName FUNC_ARRAY_LEN = FunctionName
			.getInstance("the", "\"length\"", "of", "");
	public static final List<String> STRUCT_ARRAY = Arrays.asList("array");
	public static final Map<VariableIdentifier, LiteralFunction> DEFAULT_FUNCTIONS = new HashMap<>();
	static {
		for (String suffix : new String[] { "st", "nd", "rd", "th" }) {
			addFunction(StdLibFunctions.ARRAY_ACCESS, "the", "", suffix,
					"element", "of", "");
			addFunction(StdLibFunctions.ARRAY_MOD, "Set", "the", "", suffix,
					"element", "of", "", "to", "");
		}
		addFunction(StdLibFunctions.LETTER_COMBINE, "the", "letters", "",
				"combined", "to", "form", "a", "string");
		addFunction(StdLibFunctions.PRINT, "Tell", "me", "what", "", "is");
		addFunction(StdLibFunctions.STRING_APPEND, "", "joined", "with", "");
		addFunction(StdLibFunctions.STRING_SPLIT, "", "split", "into",
				"individual", "letters");
		addFunction(StdLibFunctions.STRING_LENGTH, "the", "\"length\"", "of",
				"");
		addFunction(StdLibFunctions.LESS_THAN, "", "is", "less", "than", "");
		addFunction(StdLibFunctions.LESS_THAN_OR_EQ, "", "is", "less", "than",
				"or", "equal", "to", "");
		addFunction(StdLibFunctions.LESS_THAN_OR_EQ, "", "is", "at", "most",
				"");
		addFunction(StdLibFunctions.GREATER_THAN, "", "is", "greater", "than",
				"");
		addFunction(StdLibFunctions.GREATER_THAN_OR_EQ, "", "is", "greater",
				"than", "or", "equal", "to", "");
		addFunction(StdLibFunctions.GREATER_THAN_OR_EQ, "", "is", "at", "least",
				"");
		addFunction(StdLibFunctions.EQUALS, "", "is", "equal", "to", "");
		addFunction(StdLibFunctions.NOT_EQUALS, "", "is", "not", "equal", "to",
				"");
		addFunction(StdLibFunctions.AND, "", "and", "");
		addFunction(StdLibFunctions.OR, "", "or", "");
		addFunction(StdLibFunctions.NOT, "not", "");
		addFunction(StdLibFunctions.ARRAY_LENGTH, "the", "\"length\"", "of",
				"");
		addOperation(StdLibImplementations.PLUS, Resources.ADDITION_SIGN);
		addOperation(StdLibImplementations.MINUS, Resources.SUBTRACTION_SIGN);
		addOperation(StdLibImplementations.TIMES,
				Resources.MULTIPLICATION_SIGN);
		addOperation(StdLibImplementations.DIV, Resources.DIV_SIGN);
		addOperation(StdLibImplementations.FLOOR_DIV, Resources.FLOORDIV_SIGN);
		addOperation(StdLibImplementations.MOD, Resources.MOD_SIGN);
	}
	private static void addOperation(FunctionImplementation impl, String op) {
		addFunction(
				FunctionSynthetic.getInstance(impl, PrimitiveType.SYNTH_NUMBER,
						PrimitiveType.SYNTH_NUMBER, PrimitiveType.SYNTH_NUMBER),
				opName(op));
	}
	public static String[] opName(String op) {
		return new String[] { "", op, "", };
	}
	private static void addFunction(LiteralFunction func, String... name) {
		DEFAULT_FUNCTIONS.put(
				new FunctionSignature(FunctionName.getInstance(name), func.type)
						.identifier(),
				func);
	}
	public static FunctionName functArrayAccess(String suffix) {
		return FunctionName.getInstance("the", "", suffix, "element", "of", "");
	}
	static {
		addPair();
	}
	private static void addPair() {
		final TypeVariable _k = new TypeVariable(VariableIdentifier
				.getInstance(LiteralToken.entire("\"k\""), false));
		final TypeVariable _v = new TypeVariable(VariableIdentifier
				.getInstance(LiteralToken.entire("\"v\""), false));
		DEF_STRUCT
				.addStructure(
						new GenericStructureSignature(
								new GenericStructureType(
										Arrays.asList(
												LiteralToken.entire("pair")),
										Arrays.asList(_k, _v),
										Context.SYNTHETIC),
								Arrays.asList(
										new GenericField(
												VariableIdentifier.getInstance(
														LiteralToken
																.entire("\"key\""),
														false),
												_k),
										new GenericField(
												VariableIdentifier.getInstance(
														LiteralToken
																.entire("\"value\""),
														false),
												_v))));
		for (GenericStructureSignature sig : DEF_STRUCT.structs) {
			DEFAULT_FUNCTIONS.putAll(sig.fieldFunctions());
		}
	}
	public static Pair<FunctionName, List<Expression>> parseFunction(
			List<FunctionComponent> name, List<Expression> arguments) {
		if (FunctionName.getInstance(name)
				.equals(StdLib42.FUNC_FIELD_ACCESS_NAME_APPARENT)) {
			Optional<VariableIdentifier> field = arguments.get(0).identifier();
			if (!field.isPresent())
				ParserErrors.expectedVariableInFieldAccess(arguments.get(0));
			return Pair.of(getFieldAccess(field.get().name.token),
					Arrays.asList(arguments.get(1)));
		} else if (FunctionName.getInstance(name)
				.equals(StdLib42.FUNC_FIELD_MODIFICATION_NAME_APPARENT)) {
			Optional<VariableIdentifier> field = arguments.get(0).identifier();
			if (!field.isPresent())
				ParserErrors.expectedVariableInFieldAccess(arguments.get(0));
			return Pair.of(getFieldModification(field.get().name.token),
					Arrays.asList(arguments.get(1), arguments.get(2)));
		}
		return Pair.of(FunctionName.getInstance(name), arguments);
	}
	public static FunctionName getFieldAccess(String name) {
		return FunctionName.getInstance("the", name, "of", "");
	}
	public static FunctionName getFieldModification(String name) {
		return FunctionName.getInstance("Set", "the", name, "of", "", "to", "");
	}
	public static FunctionName functArrayModification(String suffix) {
		return FunctionName.getInstance("Set", "the", "", suffix, "element",
				"of", "", "to", "");
	}
	public static void defaultFunctions(FunctionRoster funcs) {
		for (final Entry<VariableIdentifier, LiteralFunction> f : DEFAULT_FUNCTIONS
				.entrySet())
			funcs.add(f.getKey(), f.getValue());
	}
	public static void defaultSignatures(FunctionSignatureRoster funcs) {
		for (final Entry<VariableIdentifier, LiteralFunction> f : DEFAULT_FUNCTIONS
				.entrySet())
			funcs.putReference(f.getKey(), f.getValue().type);
	}
}
