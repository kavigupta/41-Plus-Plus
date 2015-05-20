package fortytwo.compiler.parsed.statements;

import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.language.SourceCode;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.statements.IfElse;
import fortytwo.vm.statements.Statement;

public class ParsedIfElse implements ParsedStatement {
	public final ParsedExpression condition;
	public final ParsedStatement ifso, ifelse;
	public static ParsedIfElse getInstance(ParsedExpression condition,
			ParsedStatement ifso, ParsedStatement ifelse) {
		return new ParsedIfElse(condition, ifso, ifelse);
	}
	private ParsedIfElse(ParsedExpression condition, ParsedStatement ifso,
			ParsedStatement ifelse) {
		this.condition = condition;
		this.ifso = ifso;
		this.ifelse = ifelse;
	}
	@Override
	public Statement contextualize(StaticEnvironment env) {
		return IfElse.getInstance(condition.contextualize(env),
				ifso.contextualize(env), ifelse.contextualize(env));
	}
	@Override
	public SentenceType type() {
		return SentenceType.CONTROL_FLOW;
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this);
	}
}
