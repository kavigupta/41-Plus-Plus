package fortytwo.vm.statements;

import fortytwo.compiler.Context;
import fortytwo.language.SourceCode;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralBool;

public class IfElse implements Statement {
	public final Expression condition;
	public final Statement ifso, ifelse;
	private final Context context;
	public static IfElse getInstance(Expression condition, Statement ifso,
			Statement ifelse, Context context) {
		return new IfElse(condition, ifso, ifelse, context);
	}
	private IfElse(Expression condition, Statement ifso, Statement ifelse,
			Context context) {
		this.condition = condition;
		this.ifso = ifso;
		this.ifelse = ifelse;
		this.context = context;
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
	public String toSourceCode() {
		return SourceCode.display(this);
	}
	@Override
	public boolean isSimple() {
		return false;
	}
	@Override
	public Context context() {
		return context;
	}
}
