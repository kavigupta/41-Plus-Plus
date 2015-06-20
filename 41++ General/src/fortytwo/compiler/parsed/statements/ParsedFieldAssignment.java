package fortytwo.compiler.parsed.statements;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.language.SourceCode;
import fortytwo.language.field.Field;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.StructureType;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.errors.TypingErrors;
import fortytwo.vm.expressions.LiteralObject;

public class ParsedFieldAssignment extends ParsedAssignment {
	public final VariableIdentifier name, field;
	public final ParsedExpression value;
	private final Context context;
	public ParsedFieldAssignment(VariableIdentifier name,
			VariableIdentifier field, ParsedExpression parseExpression,
			Context context) {
		this.name = name;
		this.field = field;
		this.value = parseExpression;
		this.context = context;
	}
	@Override
	public boolean typeCheck(StaticEnvironment env) {
		// TODO handle when not fed a structure...
		if (!env.typeOf(field).equals(value.resolveType(env)))
			TypingErrors.fieldAssignmentTypeMismatch(env.structs
					.getStructure((StructureType) name.resolveType(env)),
					new Field(name, env.typeOf(name)), value, env);
		return true;
	}
	@Override
	public void execute(LocalEnvironment environment) {
		StaticEnvironment se = environment.staticEnvironment();
		if (!(name.resolveType(se) instanceof StructureType)) {
			// should never happen.
			return;
		}
		((LiteralObject) name.literalValue(environment)).redefine(field,
				value.literalValue(environment));
	}
	@Override
	public void clean(LocalEnvironment environment) {
		// nothing to clean.
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
