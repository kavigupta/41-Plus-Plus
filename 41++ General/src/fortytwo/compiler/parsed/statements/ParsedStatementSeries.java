package fortytwo.compiler.parsed.statements;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import fortytwo.compiler.Context;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.type.GenericType;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.errors.TypingErrors;
import fortytwo.vm.expressions.LiteralExpression;

public class ParsedStatementSeries extends ParsedStatement {
	public final List<ParsedStatement> statements;
	public static ParsedStatementSeries getInstance(ParsedStatement s,
			Context context) {
		if (s.kind() == SentenceType.COMPOUND) return (ParsedStatementSeries) s;
		return new ParsedStatementSeries(Arrays.asList(s), context);
	}
	public ParsedStatementSeries(List<ParsedStatement> statements,
			Context context) {
		super(context);
		this.statements = statements;
	}
	@Override
	public boolean typeCheck(StaticEnvironment env) {
		statements.forEach(s -> s.isTypeChecked(env));
		return true;
	}
	@Override
	public Optional<LiteralExpression> execute(LocalEnvironment environment) {
		for (final ParsedStatement s : statements) {
			final Optional<LiteralExpression> expr = s.execute(environment);
			if (expr.isPresent()) return expr;
		}
		return Optional.empty();
	}
	@Override
	public Optional<GenericType> returnType(StaticEnvironment env) {
		Optional<GenericType> type = Optional.empty();
		for (final ParsedStatement s : statements) {
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
	public void clean(LocalEnvironment environment) {
		statements.forEach(s -> s.clean(environment));
	}
	@Override
	public SentenceType kind() {
		return SentenceType.COMPOUND;
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this);
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
		final ParsedStatementSeries other = (ParsedStatementSeries) obj;
		if (statements == null) {
			if (other.statements != null) return false;
		} else if (!statements.equals(other.statements)) return false;
		return true;
	}
}
