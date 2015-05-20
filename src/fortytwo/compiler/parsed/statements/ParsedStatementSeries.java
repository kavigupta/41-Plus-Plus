package fortytwo.compiler.parsed.statements;

import java.util.List;
import java.util.stream.Collectors;

import fortytwo.language.SourceCode;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.statements.Statement;
import fortytwo.vm.statements.StatementSeries;

public class ParsedStatementSeries implements ParsedStatement {
	public final List<ParsedStatement> statements;
	public ParsedStatementSeries(List<ParsedStatement> statements) {
		this.statements = statements;
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
}
