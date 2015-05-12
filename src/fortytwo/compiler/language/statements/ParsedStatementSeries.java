package fortytwo.compiler.language.statements;

import java.util.List;

public class ParsedStatementSeries implements ParsedStatement {
	public final List<ParsedStatement> statements;
	public ParsedStatementSeries(List<ParsedStatement> statements) {
		this.statements = statements;
	}
}
