package fortytwo.compiler.language.statements;

import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.statements.Statement;

public class ParsedWhileLoop implements ParsedStatement {
	public final ParsedExpression condition;
	public final ParsedStatement ParsedStatement;
	public ParsedWhileLoop(ParsedExpression condition,
			ParsedStatement ParsedStatement) {
		this.condition = condition;
		this.ParsedStatement = ParsedStatement;
	}
	@Override
	public Statement contextualize(LocalEnvironment environment) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public SentenceType type() {
		return SentenceType.CONTROL_FLOW;
	}
}
