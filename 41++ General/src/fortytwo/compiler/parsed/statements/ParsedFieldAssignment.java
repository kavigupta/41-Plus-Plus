package fortytwo.compiler.parsed.statements;

import java.util.Optional;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.language.SourceCode;
import fortytwo.language.field.TypedVariable;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.GenericType;
import fortytwo.language.type.StructureType;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.errors.TypingErrors;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralObject;

/**
 * Represents a field assignment.
 */
public class ParsedFieldAssignment extends ParsedAssignment {
	/**
	 * The name of the variable whose field is being modified.
	 * TODO allow other things to be modified.
	 */
	public final VariableIdentifier name;
	/**
	 * The field to be set.
	 */
	public final VariableIdentifier field;
	/**
	 * The value to set the field to.
	 */
	public final Expression value;
	public ParsedFieldAssignment(VariableIdentifier name,
			VariableIdentifier field, Expression parseExpression,
			Context context) {
		super(context);
		this.name = name;
		this.field = field;
		this.value = parseExpression;
	}
	@Override
	public boolean typeCheck(StaticEnvironment env) {
		// TODO handle when not fed a structure...
		if (!env.typeOf(field).equals(value.type(env)))
			TypingErrors.fieldAssignmentTypeMismatch(
					env.structs.getStructure((StructureType) name.type(env)),
					new TypedVariable(name, env.typeOf(name)), value, env);
		return true;
	}
	@Override
	public Optional<LiteralExpression> execute(LocalEnvironment environment) {
		final StaticEnvironment se = environment.staticEnvironment();
		if (!(name.type(se) instanceof StructureType)) // should never happen.
			return Optional.empty();
		((LiteralObject) name.literalValue(environment)).redefine(field,
				value.literalValue(environment));
		return Optional.empty();
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
	public Optional<GenericType> returnType(StaticEnvironment env) {
		return Optional.empty();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (field == null ? 0 : field.hashCode());
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result + (value == null ? 0 : value.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final ParsedFieldAssignment other = (ParsedFieldAssignment) obj;
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
