package fortytwo.compiler.parsed.statements;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.language.SourceCode;
import fortytwo.language.field.Field;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.errors.TypingErrors;

public class ParsedRedefinition extends ParsedAssignment {
	public final VariableIdentifier name;
	public final ParsedExpression value;
	private final Context context;
	public ParsedRedefinition(VariableIdentifier name, ParsedExpression expr,
			Context context) {
		this.name = name;
		this.value = expr;
		this.context = context;
	}
	@Override
	public boolean typeCheck(StaticEnvironment env) {
		if (name.resolveType(env).equals(value.resolveType(env)))
			return true;
		TypingErrors.redefinitionTypeMismatch(
				new Field(name, name.resolveType(env)), value, env);
		// should never get here
		return false;
	}
	@Override
	public void execute(LocalEnvironment environment) {
		environment.vars.redefine(name, value.literalValue(environment));
	}
	@Override
	public void clean(LocalEnvironment environment) {
		// no variables created
	}
	@Override
	public String toSourceCode() {
		return SourceCode.displayRedefinition(name, value);
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
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ParsedRedefinition other = (ParsedRedefinition) obj;
		if (value == null) {
			if (other.value != null) return false;
		} else if (!value.equals(other.value)) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		return true;
	}
}
