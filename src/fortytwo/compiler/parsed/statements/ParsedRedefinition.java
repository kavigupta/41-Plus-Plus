package fortytwo.compiler.parsed.statements;

import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.language.SourceCode;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.statements.Redefinition;
import fortytwo.vm.statements.Statement;

public class ParsedRedefinition extends ParsedAssignment {
	public final VariableIdentifier name;
	public final ParsedExpression expr;
	public ParsedRedefinition(VariableIdentifier name, ParsedExpression expr) {
		this.name = name;
		this.expr = expr;
	}
	@Override
	public Statement contextualize(StaticEnvironment environment) {
		return new Redefinition(name, expr.contextualize(environment),
				environment.types);
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this);
	}
}
