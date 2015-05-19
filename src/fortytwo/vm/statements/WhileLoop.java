package fortytwo.vm.statements;

import fortytwo.language.type.PrimitiveType;
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
	}
	@Override
	public boolean typeCheck() {
		condition.typeCheck();
		if (!condition.resolveType().equals(PrimitiveType.BOOL))
			throw new RuntimeException(/* LOWPRI-E */);
		statement.typeCheck();
		return false;
	}
}
