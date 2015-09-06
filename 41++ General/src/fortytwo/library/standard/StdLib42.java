package fortytwo.library.standard;

import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;

import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.language.Language;
import fortytwo.language.field.GenericField;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.identifier.functioncomponent.FunctionComponent;
import fortytwo.language.identifier.functioncomponent.FunctionToken;
import fortytwo.language.type.*;
import fortytwo.vm.constructions.FunctionSynthetic;
import fortytwo.vm.constructions.GenericStructureSignature;
import fortytwo.vm.environment.FunctionRoster;
import fortytwo.vm.environment.FunctionSignatureRoster;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.environment.StructureRoster;
import fortytwo.vm.errors.ParserErrors;
import fortytwo.vm.expressions.LiteralFunction;

public class StdLib42 {
	public static final StructureRoster DEF_STRUCT = new StructureRoster();
	public static final FunctionName FUNC_FIELD_ACCESS_NAME_APPARENT = FunctionName
			.getInstance("the", "", "of", "");
	public static final FunctionName FUNC_ARRAY_LEN = FunctionName
			.getInstance("the", TypeVariable.LENGTH.name.name.token, "of", "");
	public static final List<String> STRUCT_ARRAY = Arrays.asList("array");
	public static final Map<FunctionName, LiteralFunction> DEFAULT_FUNCTIONS = new HashMap<>();
	static {
		// TODO for each in {st, nd, rd, th}
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
		// Arrays.asList(FunctionCompare.Comparator.values()).stream()
		// .forEach(x -> DEFAULT_FUNCTIONS.add(new FunctionCompare(x)));
	}
	private static void addFunction(LiteralFunction func, String... name) {
		DEFAULT_FUNCTIONS.put(FunctionName.getInstance(name), func);
	}
	public static FunctionName functArrayAccess(String suffix) {
		return FunctionName.getInstance("the", "", suffix, "element", "of", "");
	}
	static {
		addPair();
	}
	private static void addPair() {
		final TypeVariable _k = new TypeVariable(
				VariableIdentifier.getInstance(LiteralToken.entire("\"k\"")));
		final TypeVariable _v = new TypeVariable(
				VariableIdentifier.getInstance(LiteralToken.entire("\"v\"")));
		DEF_STRUCT
				.addStructure(new GenericStructureSignature(
						new GenericStructureType(
								Arrays.asList(LiteralToken.entire("pair")),
								Arrays.asList(_k, _v), Context.SYNTHETIC),
						Arrays.asList(
								new GenericField(
										VariableIdentifier.getInstance(
												LiteralToken.entire("\"key\"")),
								_k),
						new GenericField(
								VariableIdentifier.getInstance(
										LiteralToken.entire("\"value\"")),
								_v))));
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
		}
		return Pair.of(FunctionName.getInstance(name), arguments);
	}
	public static FunctionName getFieldAccess(String name) {
		return FunctionName.getInstance("the", name, "of", "");
	}
	public static FunctionName functArrayModification(String suffix) {
		return FunctionName.getInstance("Set", "the", "", suffix, "element",
				"of", "", "to", "");
	}
	public static Pair<LiteralFunction, ConcreteType> matchFieldAccess(
			StaticEnvironment se, FunctionName name, List<Expression> inputs) {
		if (name.function.size() != 4) return null;
		if (!name.function.get(0)
				.equals(new FunctionToken(LiteralToken.synthetic("the"))))
			return null;
		if (!(name.function.get(1).isArgument())) return null;
		if (!name.function.get(2)
				.equals(new FunctionToken(LiteralToken.synthetic("of"))))
			return null;
		if (!(name.function.get(3).isArgument())) return null;
		if (!(inputs.get(0) instanceof VariableIdentifier)) return null;
		final VariableIdentifier field = (VariableIdentifier) inputs.get(0);
		final ConcreteType type = inputs.get(1).type(se);
		if (type instanceof ArrayType) {
			if (field.equals(TypeVariable.LENGTH.name))
				return Pair.of(StdLibFunctions.ARRAY_ACCESS, new PrimitiveType(
						PrimitiveTypeWOC.NUMBER, Context.SYNTHETIC));
		} else if (type.equals(
				new PrimitiveType(PrimitiveTypeWOC.STRING, Context.SYNTHETIC)))
			return Pair.of(StdLibFunctions.STRING_LENGTH, new PrimitiveType(
					PrimitiveTypeWOC.NUMBER, Context.SYNTHETIC));
		if (!(type instanceof StructureType)) return null;
		Optional<ConcreteType> output = se.structs
				.getStructure((StructureType) type).typeof(field);
		if (!output.isPresent()) {
			// TODO handle
		}
		LiteralFunction fun = new FunctionSynthetic(
				new FunctionType(Arrays.asList(type), output.get()),
				StdLibImplementations.fieldAccess(field));
		return Pair.of(fun, output.get());
	}
	public static Optional<LiteralFunction> matchCompiledFieldAccess(
			StaticEnvironment se, FunctionName name,
			List<ConcreteType> inputs) {
		if (name.function.size() != 4) return Optional.empty();
		if (!name.function.get(0)
				.equals(new FunctionToken(LiteralToken.synthetic("the"))))
			return Optional.empty();
		if (!(name.function.get(1) instanceof FunctionToken)
				|| !Language.isValidVariableIdentifier(
						((FunctionToken) name.function.get(1)).token))
			return Optional.empty();
		if (!name.function.get(2)
				.equals(new FunctionToken(LiteralToken.synthetic("of"))))
			return Optional.empty();
		if (!(name.function.get(3).isArgument())) return Optional.empty();
		final VariableIdentifier field = VariableIdentifier
				.getInstance(((FunctionToken) name.function.get(1)).token);
		final ConcreteType type = inputs.get(0);
		if (type instanceof ArrayType) {
			if (field.equals(TypeVariable.LENGTH.name))
				return Optional.of(StdLibFunctions.ARRAY_LENGTH);
		} else if (type.equals(
				new PrimitiveType(PrimitiveTypeWOC.NUMBER, Context.SYNTHETIC)))
			return Optional.of(StdLibFunctions.STRING_LENGTH);
		if (!(type instanceof StructureType)) return Optional.empty();
		Optional<ConcreteType> output = se.structs
				.getStructure((StructureType) type).typeof(field);
		if (!output.isPresent()) {
			Optional.empty();
		}
		LiteralFunction fun = new FunctionSynthetic(
				new FunctionType(Arrays.asList(type), output.get()),
				StdLibImplementations.fieldAccess(field));
		return Optional.of(fun);
	}
	public static void defaultFunctions(FunctionRoster funcs) {
		for (final Entry<FunctionName, LiteralFunction> f : DEFAULT_FUNCTIONS
				.entrySet())
			funcs.add(new FunctionSignature(f.getKey(), f.getValue().type),
					f.getValue());
	}
	public static void defaultSignatures(FunctionSignatureRoster funcs) {
		for (final Entry<FunctionName, LiteralFunction> f : DEFAULT_FUNCTIONS
				.entrySet())
			funcs.putReference(
					new FunctionSignature(f.getKey(), f.getValue().type));
	}
}
