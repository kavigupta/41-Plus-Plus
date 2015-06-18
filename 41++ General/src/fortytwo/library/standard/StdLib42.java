package fortytwo.library.standard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lib.standard.collections.Pair;
import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.language.Language;
import fortytwo.language.field.GenericField;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.identifier.functioncomponent.FunctionArgument;
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
import fortytwo.vm.expressions.Expression;

public class StdLib42 {
	public static final StructureRoster DEF_STRUCT = new StructureRoster();
	public static final FunctionName FUNC_FIELD_ACCESS_NAME_APPARENT = FunctionName
			.getInstance("the", "", "of", "");
	public static final List<String> STRUCT_ARRAY = Arrays.asList("array");
	public static final FunctionName FUNC_PRINT = FunctionName.getInstance(
			"Tell", "me", "what", "", "is");
	public static final FunctionName FUNC_STRING_SPLIT = FunctionName
			.getInstance("", "split", "into", "individual", "letters");
	public static final FunctionName FUNC_STRING_APPEND = FunctionName
			.getInstance("", "joined", "with", "");
	public static final FunctionName FUNC_LETTER_COMBINE = FunctionName
			.getInstance("the", "letters", "", "combined", "to", "form",
					"a", "string");
	public static final List<Function42> DEFAULT_FUNCTIONS = new ArrayList<>(
			Arrays.asList(FunctionArrayAccess.ST, FunctionArrayAccess.ND,
					FunctionArrayAccess.RD, FunctionArrayAccess.TH,
					FunctionArrayModification.ST,
					FunctionArrayModification.ND,
					FunctionArrayModification.RD,
					FunctionArrayModification.TH, FunctionPrint.INSTANCE,
					FunctionStringAppend.INSTANCE,
					FunctionStringSplit.INSTANCE,
					FunctionLetterCombine.INSTANCE,
					FunctionStrlen.INSTANCE, FunctionArrayLength.INSTANCE,
					FunctionLogicalOperator.AND,
					FunctionLogicalOperator.OR,
					FunctionLogicalOperator.NOT, FunctionEquivalence.DF,
					FunctionEquivalence.EQ, FunctionEquivalence.NEQ,
					FunctionEquivalence.SA));
	static {
		Arrays.asList(FunctionCompare.Comparator.values())
				.stream()
				.forEach(x -> DEFAULT_FUNCTIONS.add(new FunctionCompare(x)));
	}
	public static FunctionName functArrayAccess(String suffix) {
		return FunctionName.getInstance("the", "", suffix, "element", "of",
				"");
	}
	static {
		addPair();
	}
	private static void addPair() {
		TypeVariable _k = new TypeVariable(
				VariableIdentifier.getInstance(LiteralToken.entire("\"k\"")));
		TypeVariable _v = new TypeVariable(
				VariableIdentifier.getInstance(LiteralToken.entire("\"v\"")));
		DEF_STRUCT.addStructure(new GenericStructureSignature(
				new GenericStructureType(Arrays.asList(LiteralToken
						.entire("pair")), Arrays.asList(_k, _v),
						Context.SYNTHETIC), Arrays.asList(
						new GenericField(VariableIdentifier
								.getInstance(LiteralToken
										.entire("\"key\"")), _k),
						new GenericField(VariableIdentifier
								.getInstance(LiteralToken
										.entire("\"value\"")), _v))));
	}
	public static Pair<FunctionName, List<ParsedExpression>> parseFunction(
			List<FunctionComponent> name, List<ParsedExpression> arguments) {
		if (FunctionName.getInstance(name).equals(
				StdLib42.FUNC_FIELD_ACCESS_NAME_APPARENT)) {
			if (!(arguments.get(0) instanceof VariableIdentifier))
				ParserErrors
						.expectedVariableInFieldAccess(arguments.get(0));
			return Pair
					.getInstance(
							getFieldAccess(((VariableIdentifier) arguments
									.get(0)).name.token), Arrays
									.asList(arguments.get(1)));
		}
		return Pair.getInstance(FunctionName.getInstance(name), arguments);
	}
	public static FunctionName getFieldAccess(String name) {
		return FunctionName.getInstance("the", name, "of", "");
	}
	public static Pair<Function42, ConcreteType> matchFieldAccess(
			StaticEnvironment se, FunctionName name, List<Expression> inputs) {
		if (name.function.size() != 4) return null;
		if (!name.function.get(0).equals(
				new FunctionToken(LiteralToken.synthetic("the"))))
			return null;
		if (!(name.function.get(1) instanceof FunctionArgument)) return null;
		if (!name.function.get(2).equals(
				new FunctionToken(LiteralToken.synthetic("of"))))
			return null;
		if (!(name.function.get(3) instanceof FunctionArgument)) return null;
		if (!(inputs.get(0) instanceof VariableIdentifier)) return null;
		VariableIdentifier field = (VariableIdentifier) inputs.get(0);
		ConcreteType type = inputs.get(1).resolveType();
		if (type instanceof ArrayType) {
			if (field.equals(TypeVariable.LENGTH.name))
				return Pair.getInstance(FunctionArrayLength.INSTANCE,
						new PrimitiveType(
								PrimitiveTypeWithoutContext.NUMBER,
								Context.SYNTHETIC));
		} else if (type.equals(new PrimitiveType(
				PrimitiveTypeWithoutContext.STRING, Context.SYNTHETIC)))
			return Pair.getInstance(FunctionStrlen.INSTANCE,
					new PrimitiveType(PrimitiveTypeWithoutContext.NUMBER,
							Context.SYNTHETIC));
		if (!(type instanceof StructureType)) return null;
		FunctionFieldAccess f = new FunctionFieldAccess(field,
				se.structs.getStructure((StructureType) type));
		return Pair.getInstance(f, f.outputType());
	}
	public static Pair<Function42, ConcreteType> matchCompiledFieldAccess(
			StaticEnvironment se, FunctionName name,
			List<ConcreteType> inputs) {
		if (name.function.size() != 4) return null;
		if (!name.function.get(0).equals(
				new FunctionToken(LiteralToken.synthetic("the"))))
			return null;
		if (!(name.function.get(1) instanceof FunctionToken)
				|| !(Language
						.isValidVariableIdentifier(((FunctionToken) name.function
								.get(1)).token))) return null;
		if (!name.function.get(2).equals(
				new FunctionToken(LiteralToken.synthetic("of"))))
			return null;
		if (!(name.function.get(3) instanceof FunctionArgument)) return null;
		VariableIdentifier field = VariableIdentifier
				.getInstance(((FunctionToken) name.function.get(1)).token);
		ConcreteType type = inputs.get(0);
		if (type instanceof ArrayType) {
			if (field.equals(TypeVariable.LENGTH.name))
				return Pair.getInstance(FunctionArrayLength.INSTANCE,
						new PrimitiveType(
								PrimitiveTypeWithoutContext.NUMBER,
								Context.SYNTHETIC));
		} else if (type.equals(new PrimitiveType(
				PrimitiveTypeWithoutContext.NUMBER, Context.SYNTHETIC)))
			return Pair.getInstance(FunctionStrlen.INSTANCE,
					new PrimitiveType(PrimitiveTypeWithoutContext.NUMBER,
							Context.SYNTHETIC));
		if (!(type instanceof StructureType)) return null;
		FunctionFieldAccess f = new FunctionFieldAccess(field,
				se.structs.getStructure((StructureType) type));
		return Pair.getInstance(f, f.outputType());
	}
	public static void defaultFunctions(FunctionRoster funcs) {
		for (Function42 f : DEFAULT_FUNCTIONS)
			funcs.add(f);
	}
	public static void defaultSignatures(FunctionSignatureRoster funcs) {
		for (Function42 f : DEFAULT_FUNCTIONS)
			funcs.funcs.add(f.signature());
	}
}
