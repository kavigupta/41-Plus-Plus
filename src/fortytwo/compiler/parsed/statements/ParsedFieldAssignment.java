package fortytwo.compiler.parsed.statements;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.expressions.UntypedExpression;
import fortytwo.language.SourceCode;
import fortytwo.language.identifier.VariableID;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.statements.FieldAssignment;
import fortytwo.vm.statements.Statement;

public class ParsedFieldAssignment extends ParsedAssignment {
	public final VariableID name, field;
	public final UntypedExpression value;
	private final Context context;
	public ParsedFieldAssignment(VariableID name,
			VariableID field, UntypedExpression parseExpression,
			Context context) {
		this.name = name;
		this.field = field;
		this.value = parseExpression;
		this.context = context;
	}
	@Override
	public Statement contextualize(StaticEnvironment environment) {
		return new FieldAssignment(
				name.contextualize(environment),
				environment.structs.typeOf(environment.typeOf(name), field),
				value.contextualize(environment), context());
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(name, field, value);
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
		result = prime * result + ((field == null) ? 0 : field.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ParsedFieldAssignment other = (ParsedFieldAssignment) obj;
		if (field == null) {
			if (other.field != null) return false;
		} else if (!field.equals(other.field)) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		if (value == null) {
			if (other.value != null) return false;
		} else if (!value.equals(other.value)) return false;
		return true;
	}
}
