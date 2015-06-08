package fortytwo.compiler.parsed.statements;

import java.util.Arrays;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.expressions.UntypedExpression;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.statements.IfElse;
import fortytwo.vm.statements.Statement;

public class ParsedIfElse implements ParsedStatement {
	public final UntypedExpression condition;
	public final ParsedStatementSeries ifso, ifelse;
	public static ParsedIfElse getInstance(UntypedExpression condition,
			ParsedStatementSeries ifso, ParsedStatementSeries ifelse) {
		return new ParsedIfElse(condition, ifso, ifelse);
	}
	private ParsedIfElse(UntypedExpression condition,
			ParsedStatementSeries ifso, ParsedStatementSeries ifelse) {
		this.condition = condition;
		this.ifso = ifso;
		this.ifelse = ifelse;
	}
	@Override
	public Statement contextualize(StaticEnvironment env) {
		Expression cond = condition.contextualize(env);
		Statement ifsoS = ifso.contextualize(StaticEnvironment.getChild(env)), ifsoE = ifelse
				.contextualize(StaticEnvironment.getChild(env));
		return IfElse.getInstance(cond, ifsoS, ifsoE, context());
	}
	@Override
	public SentenceType type() {
		return SentenceType.CONTROL_FLOW;
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
		return Context.sum(Arrays.asList(condition.context(), ifso.context(),
				ifelse.context()));
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((condition == null) ? 0 : condition.hashCode());
		result = prime * result + ((ifelse == null) ? 0 : ifelse.hashCode());
		result = prime * result + ((ifso == null) ? 0 : ifso.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ParsedIfElse other = (ParsedIfElse) obj;
		if (condition == null) {
			if (other.condition != null) return false;
		} else if (!condition.equals(other.condition)) return false;
		if (ifelse == null) {
			if (other.ifelse != null) return false;
		} else if (!ifelse.equals(other.ifelse)) return false;
		if (ifso == null) {
			if (other.ifso != null) return false;
		} else if (!ifso.equals(other.ifso)) return false;
		return true;
	}
}
