package fortytwo.vm.statements;

import java.util.List;

import fortytwo.vm.environment.LocalEnvironment;

public class StatementSeries implements Statement {
	public final List<Statement> statements;
	public StatementSeries(List<Statement> statements) {
		this.statements = statements;
	}
	@Override
	public void execute(LocalEnvironment environment) {
		statements.forEach(s -> s.execute(environment));
	}
	@Override
	public void clean(LocalEnvironment environment) {
		statements.forEach(s -> s.clean(environment));
	}
	@Override
	public boolean typeCheck() {
		statements.forEach(s -> s.typeCheck());
		return true;
	}
}
