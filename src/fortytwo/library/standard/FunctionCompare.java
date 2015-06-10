package fortytwo.library.standard;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token42;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.functioncomponent.FunctionArgument;
import fortytwo.language.identifier.functioncomponent.FunctionComponent;
import fortytwo.language.identifier.functioncomponent.FunctionToken;
import fortytwo.language.type.GenericType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypeWithoutContext;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.expressions.LiteralBool;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralNumber;

public class FunctionCompare extends Function42 {
	public static enum Comparator {
		LESS(true, false, false, "is", "less", "than"), AT_MOST(true, true,
				false, "is", "at", "most"), LESS_THAN_OR_EQUAL(true, true,
				false, "is", "less", "than", "or", "equal", "to"),
		AT_LEAST(false, true, true, "is", "at", "least"),
		GREATER_THAN_OR_EQUAL(false, true, true, "is", "greater", "than",
				"or", "equal", "to"), GREATER(false, false, true, "is",
				"greater", "than");
		public FunctionSignature sig;
		public boolean lt, eq, gt;
		private Comparator(boolean lt, boolean eq, boolean gt, String... name) {
			List<FunctionComponent> s = new ArrayList<>();
			s.add(FunctionArgument.INSTANCE);
			s.addAll(Arrays
					.asList(name)
					.stream()
					.map(x -> new FunctionToken(new Token42(x, Context
							.synthetic()))).collect(Collectors.toList()));
			s.add(FunctionArgument.INSTANCE);
			this.sig = FunctionSignature.getInstance(FunctionName
					.getInstance(s), Arrays.asList(
					new PrimitiveType(PrimitiveTypeWithoutContext.NUMBER, Context
							.synthetic()),
					new PrimitiveType(PrimitiveTypeWithoutContext.NUMBER, Context
							.synthetic())), new PrimitiveType(
					PrimitiveTypeWithoutContext.BOOL, Context.synthetic()));
			this.lt = lt;
			this.eq = eq;
			this.gt = gt;
		}
	}
	public final Comparator compare;
	public FunctionCompare(Comparator compare) {
		this.compare = compare;
	}
	@Override
	protected LiteralExpression apply(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster) {
		BigDecimal a = ((LiteralNumber) arguments.get(0)).contents;
		BigDecimal b = ((LiteralNumber) arguments.get(1)).contents;
		int comp = a.compareTo(b);
		if (comp == 0)
			return LiteralBool.getInstance(compare.eq, Context.synthetic());
		if (comp > 0)
			return LiteralBool.getInstance(compare.gt, Context.synthetic());
		return LiteralBool.getInstance(compare.lt, Context.synthetic());
	}
	@Override
	public GenericType outputType() {
		return new PrimitiveType(PrimitiveTypeWithoutContext.BOOL, Context.synthetic());
	}
	@Override
	public FunctionSignature signature() {
		return compare.sig;
	}
}
