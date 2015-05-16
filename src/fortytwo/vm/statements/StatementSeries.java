package fortytwo.vm.statements;

import java.util.List;

import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.VariableTypeRoster;

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
	public boolean typeCheck(VariableTypeRoster typeRoster) {
		statements.forEach(s -> s.typeCheck(typeRoster));
		return true;
	}
}
