package fortytwo.compiler.parsed.statements;

import static fortytwo.language.Resources.*;

import java.util.Arrays;
import java.util.Optional;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.type.GenericType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypeWOC;
import fortytwo.vm.environment.OrderedEnvironment;
import fortytwo.vm.environment.TypeEnvironment;
import fortytwo.vm.errors.TypingErrors;
import fortytwo.vm.expressions.LiteralBool;
import fortytwo.vm.expressions.LiteralExpression;

/**
 * Represents an if else construct with a condition, and two sets of statments
 * to execute.
 */
public class IfElse extends Statement {
	/**
	 * The condition on which to execute either the {@code if so} or
	 * {@code if else} block.
	 */
	private final Expression condition;
	/**
	 * Executed if {@code condition} resolves to {@code true}.
	 */
	private final Suite ifso;
	/**
	 * Executed if {@code condition} resolves to {@code false}.
	 */
	private final Suite ifelse;
	public static IfElse getInstance(Expression condition, Suite ifso,
			Suite ifelse) {
		return new IfElse(condition, ifso, ifelse);
	}
	private IfElse(Expression condition, Suite ifso, Suite ifelse) {
		super(Context.sum(Arrays.asList(condition.context(), ifso.context(),
				ifelse.context())));
		this.condition = condition;
		this.ifso = ifso;
		this.ifelse = ifelse;
	}
	@Override
	public Optional<LiteralExpression> execute(OrderedEnvironment environment) {
		if (((LiteralBool) condition.literalValue(environment)).contents) {
			final Optional<LiteralExpression> ret = ifso.execute(environment);
			if (ret.isPresent()) return ret;
			ifso.clean(environment);
		} else {
			final Optional<LiteralExpression> ret = ifelse.execute(environment);
			if (ret.isPresent()) return ret;
			ifelse.clean(environment);
		}
		return Optional.empty();
	}
	@Override
	public Optional<GenericType> returnType(TypeEnvironment env) {
		final Optional<GenericType> a = ifso.returnType(env),
				b = ifelse.returnType(env);
		if (!a.isPresent() && !b.isPresent()) return Optional.empty();
		if (a.isPresent() && b.isPresent()) {
			if (a.get().equals(b.get())) return a;
			TypingErrors.inconsistentBranchTyping("if statement", a.get(),
					b.get(), context());
		}
		return a.isPresent() ? a : b;
	}
	@Override
	public void clean(OrderedEnvironment environment) {
		// Forms a closure, no need to clean once done
	}
	@Override
	public boolean typeCheck(TypeEnvironment env) {
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
		return IF + " " + condition.toSourceCode() + END_OF_CONTROL_STATEMENT
				+ SourceCode.wrapInBraces(ifso)
				+ (ifelse.isNoop() ? ""
						: END_OF_SENTENCE + NEWLINE + OTHERWISE
								+ END_OF_CONTROL_STATEMENT + NEWLINE
								+ INDENTATION_UNIT + ifelse.toSourceCode());
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
				+ (condition == null ? 0 : condition.hashCode());
		result = prime * result + (ifelse == null ? 0 : ifelse.hashCode());
		result = prime * result + (ifso == null ? 0 : ifso.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final IfElse other = (IfElse) obj;
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
