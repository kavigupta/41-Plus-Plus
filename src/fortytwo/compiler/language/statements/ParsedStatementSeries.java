package fortytwo.compiler.language.statements;

import java.util.List;

import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.statements.Statement;

public class ParsedStatementSeries implements ParsedStatement {
	public final List<ParsedStatement> statements;
	public ParsedStatementSeries(List<ParsedStatement> statements) {
		this.statements = statements;
	}
	@Override
	public Statement contextualize(LocalEnvironment environment) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public SentenceType type() {
		return SentenceType.COMPOUND;
	}
}
