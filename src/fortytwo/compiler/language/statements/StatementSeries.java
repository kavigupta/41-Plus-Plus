package fortytwo.compiler.language.statements;

import java.util.List;

public class StatementSeries implements Statement {
	public final List<Statement> statements;
	public StatementSeries(List<Statement> statements) {
		this.statements = statements;
	}
}
