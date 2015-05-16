package fortytwo.vm.statements;

import fortytwo.compiler.language.identifier.TypeIdentifier;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.VariableTypeRoster;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralBool;

public class IfElse implements Statement {
	public final Expression condition;
	public final Statement ifso, ifelse;
	public static IfElse getInstance(Expression condition, Statement ifso,
			Statement ifelse) {
		return new IfElse(condition, ifso, ifelse);
	}
	private IfElse(Expression condition, Statement ifso, Statement ifelse) {
		this.condition = condition;
		this.ifso = ifso;
		this.ifelse = ifelse;
	}
	@Override
	public void execute(LocalEnvironment environment) {
		if (((LiteralBool) condition.literalValue(environment)).contents)
			ifso.execute(environment);
		else ifelse.execute(environment);
	}
	@Override
	public boolean typeCheck(VariableTypeRoster typeRoster) {
		if (condition.resolveType(typeRoster).equals(
				TypeIdentifier.getInstance("bool"))) return true;
		throw new RuntimeException(/* LOWPRI-E */);
	}
}
