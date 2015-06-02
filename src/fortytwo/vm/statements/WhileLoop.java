package fortytwo.vm.statements;

import java.util.Arrays;

import fortytwo.compiler.Context;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypes;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.errors.TypingErrors;
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
	public boolean typeCheck() {
		condition.typeCheck();
		if (!condition.resolveType()
				.equals(new PrimitiveType(PrimitiveTypes.BOOL, Context
						.synthetic())))
			TypingErrors.expectedBoolInCondition(false, condition);
		statement.typeCheck();
		return false;
	}
	@Override
	public String toSourceCode() {
		// TODO Auto-generated method stub
		return null;
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
