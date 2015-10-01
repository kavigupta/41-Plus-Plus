package fortytwo.compiler.parsed.statements;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import fortytwo.compiler.Context;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.type.GenericType;
import fortytwo.vm.environment.OrderedEnvironment;
import fortytwo.vm.environment.type.AbstractTypeEnvironment;
import fortytwo.vm.errors.TypingErrors;
import fortytwo.vm.expressions.LiteralExpression;

/**
 * Represents a series of sentences.
 */
public class Suite extends Statement {
	/**
	 * The sentences to be executed.
	 */
	private final List<Statement> statements;
	/**
	 * Gets an instance of a {@code Suite} that contains the single statement.
	 * If the given statement is already a suite, it is returned.
	 * 
	 * @param s
	 *        the statement to wrap
	 * @param context
	 *        the context representing the suite
	 */
	public static Suite getInstance(Statement s, Context context) {
		if (s.kind() == SentenceType.COMPOUND) return (Suite) s;
		return new Suite(Arrays.asList(s), context);
	}
	/**
	 * Generates a suite with the given list of statements and the given context
	 */
	public Suite(List<Statement> statements, Context context) {
		super(context);
		this.statements = statements;
	}
	/**
	 * @return whether or not this suite contains no statements, and is
	 *         therefore a no-op
	 */
	public boolean isNoop() {
		return statements.size() == 0;
	}
	@Override
	public boolean typeCheck(AbstractTypeEnvironment env) {
		statements.forEach(s -> s.isTypeChecked(env));
		return true;
	}
	@Override
	public Optional<LiteralExpression> execute(OrderedEnvironment environment) {
		for (final Statement s : statements) {
			final Optional<LiteralExpression> expr = s.execute(environment);
			if (expr.isPresent()) return expr;
		}
		return Optional.empty();
	}
	@Override
	public Optional<GenericType> returnType(AbstractTypeEnvironment env) {
		Optional<GenericType> type = Optional.empty();
		for (final Statement s : statements) {
			s.typeCheck(env);
			final Optional<GenericType> state = s.returnType(env);
			if (!state.isPresent()) continue;
			if (!type.isPresent()) {
				type = state;
				continue;
			}
			if (type.get().equals(state.get())) continue;
			TypingErrors.inconsistentBranchTyping("suite", type.get(),
					state.get(), context());
		}
		return type;
	}
	@Override
	public void clean(OrderedEnvironment environment) {
		statements.forEach(s -> s.clean(environment));
	}
	@Override
	public SentenceType kind() {
		return SentenceType.COMPOUND;
	}
	@Override
	public String toSourceCode() {
		return SourceCode.displaySeries(statements);
	}
	@Override
	public boolean isSimple() {
		if (statements.size() > 1) return false;
		if (statements.size() == 0) return true;
		return statements.get(0).isSimple();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (statements == null ? 0 : statements.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final Suite other = (Suite) obj;
		if (statements == null) {
			if (other.statements != null) return false;
		} else if (!statements.equals(other.statements)) return false;
		return true;
	}
}
