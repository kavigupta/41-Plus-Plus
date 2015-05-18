package fortytwo.compiler.parsed.statements;

import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.statements.Statement;
import fortytwo.vm.statements.WhileLoop;

public class ParsedWhileLoop implements ParsedStatement {
	public final ParsedExpression condition;
	public final ParsedStatement statement;
	public ParsedWhileLoop(ParsedExpression condition,
			ParsedStatement ParsedStatement) {
		this.condition = condition;
		this.statement = ParsedStatement;
	}
	@Override
	public Statement contextualize(LocalEnvironment environment) {
		return new WhileLoop(condition.contextualize(environment),
				statement.contextualize(environment));
	}
	@Override
	public SentenceType type() {
		return SentenceType.CONTROL_FLOW;
	}
}
