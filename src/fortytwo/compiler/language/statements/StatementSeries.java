package fortytwo.compiler.language.statements;

import java.util.List;

public class StatementSeries implements ParsedStatement {
	public final List<ParsedStatement> statements;
	public StatementSeries(List<ParsedStatement> statements) {
		this.statements = statements;
	}
}
