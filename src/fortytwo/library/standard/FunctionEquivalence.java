package fortytwo.library.standard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token42;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.identifier.functioncomponent.FunctionArgument;
import fortytwo.language.identifier.functioncomponent.FunctionComponent;
import fortytwo.language.identifier.functioncomponent.FunctionToken;
import fortytwo.language.type.GenericType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypeWithoutContext;
import fortytwo.language.type.TypeVariable;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.expressions.LiteralBool;
import fortytwo.vm.expressions.LiteralExpression;

public class FunctionEquivalence extends Function42 {
	public static final TypeVariable TO_BE_COMPARED = new TypeVariable(
			VariableIdentifier.getInstance(new Token42(
					"\"FunctionEquivalence_compare\"", Context
							.entire("\"FunctionEquivalence_compare\""))));
	public static enum Comparator {
		EQUALS(true, "is", "equal", "to"), NOT_EQUALS(false, "is", "not",
				"equal", "to"), SAME_AS(true, "is", "the", "same", "as"),
		DIFFERENT_FROM(false, "is", "different", "from");
		public FunctionSignature sig;
		public boolean eq;
		private Comparator(boolean eq, String... name) {
			List<FunctionComponent> s = new ArrayList<>();
			s.add(FunctionArgument.INSTANCE);
			s.addAll(Arrays
					.asList(name)
					.stream()
					.map(x -> new FunctionToken(new Token42(x,
							Context.SYNTHETIC)))
					.collect(Collectors.toList()));
			s.add(FunctionArgument.INSTANCE);
			this.sig = FunctionSignature.getInstance(FunctionName
					.getInstance(s), Arrays.asList(TO_BE_COMPARED,
					TO_BE_COMPARED), new PrimitiveType(
					PrimitiveTypeWithoutContext.BOOL, Context.SYNTHETIC));
			this.eq = eq;
		}
	}
	public static final FunctionEquivalence EQ = new FunctionEquivalence(
			Comparator.EQUALS);
	public static final FunctionEquivalence SA = new FunctionEquivalence(
			Comparator.SAME_AS);
	public static final FunctionEquivalence NEQ = new FunctionEquivalence(
			Comparator.NOT_EQUALS);
	public static final FunctionEquivalence DF = new FunctionEquivalence(
			Comparator.DIFFERENT_FROM);
	public final Comparator compare;
	private FunctionEquivalence(Comparator compare) {
		this.compare = compare;
	}
	@Override
	protected LiteralExpression apply(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster) {
		return LiteralBool.getInstance(
				arguments.get(0).equals(arguments.get(1)) == compare.eq,
				Context.SYNTHETIC);
	}
	@Override
	public GenericType outputType() {
		return new PrimitiveType(PrimitiveTypeWithoutContext.BOOL,
				Context.SYNTHETIC);
	}
	@Override
	public FunctionSignature signature() {
		return compare.sig;
	}
}
