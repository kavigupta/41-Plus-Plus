package fortytwo.compiler.parsed.statements;

import java.util.Arrays;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypeWithoutContext;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.errors.TypingErrors;
import fortytwo.vm.expressions.LiteralBool;

public class ParsedIfElse extends ParsedStatement {
	public final Expression condition;
	public final ParsedStatementSeries ifso, ifelse;
	public static ParsedIfElse getInstance(Expression condition,
			ParsedStatementSeries ifso, ParsedStatementSeries ifelse) {
		return new ParsedIfElse(condition, ifso, ifelse);
	}
	private ParsedIfElse(Expression condition, ParsedStatementSeries ifso,
			ParsedStatementSeries ifelse) {
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
	public boolean typeCheck1(StaticEnvironment env) {
		if (condition.type(env).equals(
				new PrimitiveType(PrimitiveTypeWithoutContext.BOOL,
						Context.SYNTHETIC))) return true;
		TypingErrors.expectedBoolInCondition(true, condition, env);
		// unreachable
		return false;
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
