package fortytwo.vm.statements;

import java.util.List;
import java.util.stream.Collectors;

import fortytwo.compiler.Context;
import fortytwo.language.SourceCode;
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
	public String toSourceCode() {
		return SourceCode.display(this);
	}
	@Override
	public boolean isSimple() {
		return false;
	}
	@Override
	public Context context() {
		return Context.sum(statements.stream().map(Statement::context)
				.collect(Collectors.toList()));
	}
}
