package fortytwo.library.standard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.language.Language;
import fortytwo.language.field.GenericField;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.identifier.functioncomponent.FunctionComponent;
import fortytwo.language.identifier.functioncomponent.FunctionToken;
import fortytwo.language.type.*;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.constructions.GenericStructureSignature;
import fortytwo.vm.environment.FunctionRoster;
import fortytwo.vm.environment.FunctionSignatureRoster;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.environment.StructureRoster;
import fortytwo.vm.errors.ParserErrors;

public class StdLib42 {
	public static final StructureRoster DEF_STRUCT = new StructureRoster();
	public static final FunctionName FUNC_FIELD_ACCESS_NAME_APPARENT = FunctionName
			.getInstance("the", "", "of", "");
	public static final List<String> STRUCT_ARRAY = Arrays.asList("array");
	public static final FunctionName FUNC_PRINT = FunctionName
			.getInstance("Tell", "me", "what", "", "is");
	public static final FunctionName FUNC_STRING_SPLIT = FunctionName
			.getInstance("", "split", "into", "individual", "letters");
	public static final FunctionName FUNC_STRING_APPEND = FunctionName
			.getInstance("", "joined", "with", "");
	public static final FunctionName FUNC_LETTER_COMBINE = FunctionName
			.getInstance("the", "letters", "", "combined", "to", "form", "a",
					"string");
	public static final List<Function42> DEFAULT_FUNCTIONS = new ArrayList<>(
			Arrays.asList(FunctionArrayAccess.ST, FunctionArrayAccess.ND,
					FunctionArrayAccess.RD, FunctionArrayAccess.TH,
					FunctionArrayModification.ST, FunctionArrayModification.ND,
					FunctionArrayModification.RD, FunctionArrayModification.TH,
					FunctionPrint.INSTANCE, FunctionStringAppend.INSTANCE,
					FunctionStringSplit.INSTANCE,
					FunctionLetterCombine.INSTANCE, FunctionStrlen.INSTANCE,
					FunctionArrayLength.INSTANCE, FunctionLogicalOperator.AND,
					FunctionLogicalOperator.OR, FunctionLogicalOperator.NOT,
					FunctionEquivalence.DF, FunctionEquivalence.EQ,
					FunctionEquivalence.NEQ, FunctionEquivalence.SA));
	static {
		Arrays.asList(FunctionCompare.Comparator.values()).stream()
				.forEach(x -> DEFAULT_FUNCTIONS.add(new FunctionCompare(x)));
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
	public static Pair<Function42, ConcreteType> matchFieldAccess(
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
				return Pair.of(FunctionArrayLength.INSTANCE, new PrimitiveType(
						PrimitiveTypeWOC.NUMBER, Context.SYNTHETIC));
		} else if (type.equals(
				new PrimitiveType(PrimitiveTypeWOC.STRING, Context.SYNTHETIC)))
			return Pair.of(FunctionStrlen.INSTANCE, new PrimitiveType(
					PrimitiveTypeWOC.NUMBER, Context.SYNTHETIC));
		if (!(type instanceof StructureType)) return null;
		final FunctionFieldAccess f = new FunctionFieldAccess(field,
				se.structs.getStructure((StructureType) type));
		return Pair.of(f, f.outputType());
	}
	public static Pair<Function42, ConcreteType> matchCompiledFieldAccess(
			StaticEnvironment se, FunctionName name,
			List<ConcreteType> inputs) {
		if (name.function.size() != 4) return null;
		if (!name.function.get(0)
				.equals(new FunctionToken(LiteralToken.synthetic("the"))))
			return null;
		if (!(name.function.get(1) instanceof FunctionToken)
				|| !Language.isValidVariableIdentifier(
						((FunctionToken) name.function.get(1)).token))
			return null;
		if (!name.function.get(2)
				.equals(new FunctionToken(LiteralToken.synthetic("of"))))
			return null;
		if (!(name.function.get(3).isArgument())) return null;
		final VariableIdentifier field = VariableIdentifier
				.getInstance(((FunctionToken) name.function.get(1)).token);
		final ConcreteType type = inputs.get(0);
		if (type instanceof ArrayType) {
			if (field.equals(TypeVariable.LENGTH.name))
				return Pair.of(FunctionArrayLength.INSTANCE, new PrimitiveType(
						PrimitiveTypeWOC.NUMBER, Context.SYNTHETIC));
		} else if (type.equals(
				new PrimitiveType(PrimitiveTypeWOC.NUMBER, Context.SYNTHETIC)))
			return Pair.of(FunctionStrlen.INSTANCE, new PrimitiveType(
					PrimitiveTypeWOC.NUMBER, Context.SYNTHETIC));
		if (!(type instanceof StructureType)) return null;
		final FunctionFieldAccess f = new FunctionFieldAccess(field,
				se.structs.getStructure((StructureType) type));
		return Pair.of(f, f.outputType());
	}
	public static void defaultFunctions(FunctionRoster funcs) {
		for (final Function42 f : DEFAULT_FUNCTIONS)
			funcs.add(f);
	}
	public static void defaultSignatures(FunctionSignatureRoster funcs) {
		for (final Function42 f : DEFAULT_FUNCTIONS)
			funcs.funcs.add(f.signature());
	}
}
