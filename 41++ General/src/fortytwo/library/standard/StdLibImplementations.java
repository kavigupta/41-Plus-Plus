package fortytwo.library.standard;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypeWOC;
import fortytwo.vm.VirtualMachine;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.expressions.*;
import fortytwo.vm.expressions.LiteralFunction.FunctionImplementation;

public class StdLibImplementations {
	public static LiteralExpression arrayAccess(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster) {
		final LiteralArray array = (LiteralArray) arguments.get(1);
		final LiteralNumber index = (LiteralNumber) arguments.get(0);
		return array.get(index.contents.intValue(), Context.sum(arguments));
	}
	public static LiteralExpression arrayLength(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster) {
		return new LiteralNumber(
				BigDecimal.valueOf(((LiteralArray) arguments.get(0)).length()),
				Context.SYNTHETIC);
	}
	public static LiteralExpression arrayModification(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster) {
		final LiteralArray array = (LiteralArray) arguments.get(1);
		final LiteralNumber index = (LiteralNumber) arguments.get(0);
		final LiteralExpression value = arguments.get(2);
		array.set(index.contents.intValue(), value, Context.sum(arguments));
		return LiteralVoid.INSTANCE;
	}
	public static FunctionImplementation fieldAccess(VariableIdentifier field) {
		return (env, arguments, roster) -> ((LiteralObject) arguments.get(0))
				.valueOf(field);
	}
	public static LiteralExpression letterCombine(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster) {
		final LiteralArray array = (LiteralArray) arguments.get(0);
		final char[] c = new char[array.length()];
		for (int i = 0; i < array.length(); i++)
			// pass a synthetic context because we know there will be no
			// error.
			c[i] = ((LiteralString) array.get(i + 1,
					Context.SYNTHETIC)).contents.token.charAt(0);
		return new LiteralString(LiteralToken.synthetic(new String(c)));
	}
	public static LiteralExpression print(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster) {
		VirtualMachine.displayLine(arguments.get(0).toSourceCode());
		return LiteralVoid.INSTANCE;
	}
	public static LiteralString stringAppend(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster) {
		final LiteralToken a = ((LiteralString) arguments.get(0)).contents;
		final LiteralToken b = ((LiteralString) arguments.get(1)).contents;
		return new LiteralString(LiteralToken.append(a, b));
	}
	public static LiteralExpression stringSplit(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster) {
		final LiteralToken token = ((LiteralString) arguments.get(0)).contents;
		final LiteralArray array = new LiteralArray(
				new PrimitiveType(PrimitiveTypeWOC.STRING, Context.SYNTHETIC),
				token.token.length(), Context.SYNTHETIC);
		for (int i = 0; i < token.token.length(); i++)
			array.set(i + 1, new LiteralString(token.subToken(i, i + 1)),
					Context.sum(arguments));
		return array;
	}
	public static LiteralExpression stringLength(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster) {
		return new LiteralNumber(BigDecimal.valueOf(
				((LiteralString) arguments.get(0)).contents.token.length()),
				Context.SYNTHETIC);
	}
	public static FunctionImplementation logicalOperator(
			Function<boolean[], Boolean> op) {
		return (env, arguments, roster) -> {
			final boolean[] array = new boolean[arguments.size()];
			for (int k = 0; k < arguments.size(); k++)
				array[k] = ((LiteralBool) arguments.get(k)).contents;
			return LiteralBool.getInstance(op.apply(array), Context.SYNTHETIC);
		};
	}
	public static FunctionImplementation comparisonOperator(boolean lt,
			boolean eq, boolean gt) {
		return (env, arguments, roster) -> {
			final BigDecimal a = ((LiteralNumber) arguments.get(0)).contents;
			final BigDecimal b = ((LiteralNumber) arguments.get(1)).contents;
			final int comp = a.compareTo(b);
			if (comp == 0)
				return LiteralBool.getInstance(eq, Context.SYNTHETIC);
			if (comp > 0) return LiteralBool.getInstance(gt, Context.SYNTHETIC);
			return LiteralBool.getInstance(lt, Context.SYNTHETIC);
		};
	}
	public static FunctionImplementation equalityOperator(boolean isEqual) {
		return (env, arguments, roster) -> {
			return LiteralBool.getInstance(
					arguments.get(0).equals(arguments.get(1)) == isEqual,
					Context.SYNTHETIC);
		};
	}
}
