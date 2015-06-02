package fortytwo.compiler.parsed.statements;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fortytwo.compiler.Context;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.statements.Statement;
import fortytwo.vm.statements.StatementSeries;

public class ParsedStatementSeries implements ParsedStatement {
	public final List<ParsedStatement> statements;
	private final Context context;
	public static ParsedStatementSeries getInstance(ParsedStatement s,
			Context context) {
		if (s instanceof ParsedStatementSeries)
			return (ParsedStatementSeries) s;
		return new ParsedStatementSeries(Arrays.asList(s), context);
	}
	public ParsedStatementSeries(List<ParsedStatement> statements,
			Context context) {
		this.statements = statements;
		this.context = context;
	}
	@Override
	public Statement contextualize(StaticEnvironment env) {
		return new StatementSeries(statements.stream()
				.map(s -> s.contextualize(env))
				.collect(Collectors.toList()));
	}
	@Override
	public SentenceType type() {
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
	public Context context() {
		return context;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((statements == null) ? 0 : statements.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ParsedStatementSeries other = (ParsedStatementSeries) obj;
		if (statements == null) {
			if (other.statements != null) return false;
		} else if (!statements.equals(other.statements)) return false;
		return true;
	}
}
