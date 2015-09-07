package fortytwo.library.standard;

import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ArrayType;
import fortytwo.language.type.GenericArrayType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.TypeVariable;
import fortytwo.vm.constructions.FunctionSynthetic;
import fortytwo.vm.expressions.LiteralFunction;

class StdLibFunctions {
	private static final TypeVariable TYPE_PARAM = new TypeVariable(
			VariableIdentifier.getInstance(LiteralToken.synthetic("\"T\""),false));
	public static final LiteralFunction ARRAY_ACCESS = FunctionSynthetic
			.getInstance(StdLibImplementations::arrayAccess,
					PrimitiveType.SYNTH_NUMBER,
					new GenericArrayType(TYPE_PARAM, Context.SYNTHETIC),
					TYPE_PARAM);
	public static final LiteralFunction ARRAY_MOD = FunctionSynthetic
			.getInstance(StdLibImplementations::arrayModification,
					PrimitiveType.SYNTH_NUMBER,
					new GenericArrayType(TYPE_PARAM, Context.SYNTHETIC),
					TYPE_PARAM, PrimitiveType.SYNTH_VOID);
	public static final LiteralFunction ARRAY_LENGTH = FunctionSynthetic
			.getInstance(StdLibImplementations::arrayLength,
					new GenericArrayType(TYPE_PARAM, Context.SYNTHETIC),
					PrimitiveType.SYNTH_NUMBER);
	public static final LiteralFunction LETTER_COMBINE = FunctionSynthetic
			.getInstance(StdLibImplementations::letterCombine,
					new ArrayType(PrimitiveType.SYNTH_STRING,
							Context.SYNTHETIC),
					PrimitiveType.SYNTH_STRING);
	public static final LiteralFunction PRINT = FunctionSynthetic.getInstance(
			StdLibImplementations::print, TYPE_PARAM, PrimitiveType.SYNTH_VOID);
	public static final LiteralFunction STRING_APPEND = FunctionSynthetic
			.getInstance(StdLibImplementations::stringAppend,
					PrimitiveType.SYNTH_STRING, PrimitiveType.SYNTH_STRING,
					PrimitiveType.SYNTH_STRING);
	public static final LiteralFunction STRING_SPLIT = FunctionSynthetic
			.getInstance(StdLibImplementations::stringSplit,
					PrimitiveType.SYNTH_STRING, new ArrayType(
							PrimitiveType.SYNTH_STRING, Context.SYNTHETIC));
	public static final LiteralFunction STRING_LENGTH = FunctionSynthetic
			.getInstance(StdLibImplementations::stringLength,
					PrimitiveType.SYNTH_STRING, PrimitiveType.SYNTH_NUMBER);
	public static final LiteralFunction LESS_THAN = FunctionSynthetic
			.getInstance(
					StdLibImplementations.comparisonOperator(true, false,
							false),
					PrimitiveType.SYNTH_NUMBER, PrimitiveType.SYNTH_NUMBER,
					PrimitiveType.SYNTH_BOOL);
	public static final LiteralFunction LESS_THAN_OR_EQ = FunctionSynthetic
			.getInstance(
					StdLibImplementations.comparisonOperator(true, true, false),
					PrimitiveType.SYNTH_NUMBER, PrimitiveType.SYNTH_NUMBER,
					PrimitiveType.SYNTH_BOOL);
	public static final LiteralFunction GREATER_THAN = FunctionSynthetic
			.getInstance(
					StdLibImplementations.comparisonOperator(false, false,
							true),
					PrimitiveType.SYNTH_NUMBER, PrimitiveType.SYNTH_NUMBER,
					PrimitiveType.SYNTH_BOOL);
	public static final LiteralFunction GREATER_THAN_OR_EQ = FunctionSynthetic
			.getInstance(
					StdLibImplementations.comparisonOperator(false, true, true),
					PrimitiveType.SYNTH_NUMBER, PrimitiveType.SYNTH_NUMBER,
					PrimitiveType.SYNTH_BOOL);
	public static final LiteralFunction EQUALS = FunctionSynthetic.getInstance(
			StdLibImplementations.equalityOperator(true), TYPE_PARAM,
			TYPE_PARAM, PrimitiveType.SYNTH_BOOL);
	public static final LiteralFunction NOT_EQUALS = FunctionSynthetic
			.getInstance(StdLibImplementations.equalityOperator(false),
					TYPE_PARAM, TYPE_PARAM, PrimitiveType.SYNTH_BOOL);
	public static final LiteralFunction AND = FunctionSynthetic.getInstance(
			StdLibImplementations.logicalOperator(x -> x[0] && x[1]),
			PrimitiveType.SYNTH_BOOL, PrimitiveType.SYNTH_BOOL,
			PrimitiveType.SYNTH_BOOL);
	public static final LiteralFunction OR = FunctionSynthetic.getInstance(
			StdLibImplementations.logicalOperator(x -> x[0] || x[1]),
			PrimitiveType.SYNTH_BOOL, PrimitiveType.SYNTH_BOOL,
			PrimitiveType.SYNTH_BOOL);
	public static final LiteralFunction NOT = FunctionSynthetic.getInstance(
			StdLibImplementations.logicalOperator(x -> !x[0]),
			PrimitiveType.SYNTH_BOOL, PrimitiveType.SYNTH_BOOL);
}
