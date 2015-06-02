package fortytwo.compiler.parsed.statements;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.language.SourceCode;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.statements.Redefinition;
import fortytwo.vm.statements.Statement;

public class ParsedRedefinition extends ParsedAssignment {
	public final VariableIdentifier name;
	public final ParsedExpression expr;
	private final Context context;
	public ParsedRedefinition(VariableIdentifier name, ParsedExpression expr,
			Context context) {
		this.name = name;
		this.expr = expr;
		this.context = context;
	}
	@Override
	public Statement contextualize(StaticEnvironment environment) {
		return new Redefinition(name.contextualize(environment),
				expr.contextualize(environment), context());
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this);
	}
	@Override
	public boolean isSimple() {
		return true;
	}
	@Override
	public Context context() {
		return context;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expr == null) ? 0 : expr.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ParsedRedefinition other = (ParsedRedefinition) obj;
		if (expr == null) {
			if (other.expr != null) return false;
		} else if (!expr.equals(other.expr)) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		return true;
	}
}
