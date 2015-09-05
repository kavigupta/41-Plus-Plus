package fortytwo.compiler.parsed.statements;

import java.util.Arrays;
import java.util.Optional;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.type.GenericType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypeWOC;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.errors.TypingErrors;
import fortytwo.vm.expressions.LiteralBool;
import fortytwo.vm.expressions.LiteralExpression;

public class ParsedIfElse extends ParsedStatement {
	public final Expression condition;
	public final ParsedStatementSeries ifso, ifelse;
	public static ParsedIfElse getInstance(Expression condition,
			ParsedStatementSeries ifso, ParsedStatementSeries ifelse) {
		return new ParsedIfElse(condition, ifso, ifelse);
	}
	private ParsedIfElse(Expression condition, ParsedStatementSeries ifso,
			ParsedStatementSeries ifelse) {
		super(Context.sum(Arrays.asList(condition.context(), ifso.context(),
				ifelse.context())));
		this.condition = condition;
		this.ifso = ifso;
		this.ifelse = ifelse;
	}
	@Override
	public Optional<LiteralExpression> execute(LocalEnvironment environment) {
		if (((LiteralBool) condition.literalValue(environment)).contents) {
			Optional<LiteralExpression> ret = ifso.execute(environment);
			if (ret.isPresent()) return ret;
			ifso.clean(environment);
		} else {
			Optional<LiteralExpression> ret = ifelse.execute(environment);
			if (ret.isPresent()) return ret;
			ifelse.clean(environment);
		}
		return Optional.empty();
	}
	@Override
	public Optional<GenericType> returnType(StaticEnvironment env) {
		Optional<GenericType> a = ifso.returnType(env),
				b = ifelse.returnType(env);
		if (!a.isPresent() && !b.isPresent()) return Optional.empty();
		if (a.isPresent() && b.isPresent()) {
			if (a.get().equals(b.get())) return a;
			TypingErrors.inconsistentBranchTyping("if statement", a.get(),
					b.get(), this.context());
		}
		return a.isPresent() ? a : b;
	}
	@Override
	public void clean(LocalEnvironment environment) {
		// Forms a closure, no need to clean once done
	}
	@Override
	public boolean typeCheck(StaticEnvironment env) {
		if (condition.type(env).equals(
				new PrimitiveType(PrimitiveTypeWOC.BOOL, Context.SYNTHETIC)))
			return true;
		returnType(env);
		TypingErrors.expectedBoolInCondition(true, condition, env);
		// unreachable
		return false;
	}
	@Override
	public SentenceType kind() {
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
