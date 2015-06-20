package fortytwo.vm.statements;

import java.util.Arrays;

import fortytwo.compiler.Context;
import fortytwo.language.SourceCode;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralBool;

public class WhileLoop implements Statement {
	public final Expression condition;
	public final Statement statement;
	public WhileLoop(Expression condition, Statement parsedStatement) {
		this.condition = condition;
		statement = parsedStatement;
	}
	@Override
	public void execute(LocalEnvironment environment) {
		while (((LiteralBool) condition.literalValue(environment)).contents) {
			statement.execute(environment);
		}
		statement.clean(environment);
	}
	@Override
	public void clean(LocalEnvironment environment) {
		// forms a closure, no need to clean
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
		return Context.sum(Arrays.asList(condition.context(),
				statement.context()));
	}
}
