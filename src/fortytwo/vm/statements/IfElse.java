package fortytwo.vm.statements;

import fortytwo.language.type.PrimitiveType;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.errors.TypingErrors;
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
		if (((LiteralBool) condition.literalValue(environment)).contents) {
			ifso.execute(environment);
			ifso.clean(environment);
		} else {
			ifelse.execute(environment);
			ifelse.clean(environment);
		}
	}
	@Override
	public void clean(LocalEnvironment environment) {
		// Forms a closure, no need to clean once done
	}
	@Override
	public boolean typeCheck() {
		if (condition.resolveType().equals(PrimitiveType.BOOL)) return true;
		TypingErrors.ifConditionNonBool(condition);
		// unreachable
		return false;
	}
}
