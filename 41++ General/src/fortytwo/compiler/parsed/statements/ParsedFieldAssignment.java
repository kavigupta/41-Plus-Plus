package fortytwo.compiler.parsed.statements;

import java.util.Optional;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.language.SourceCode;
import fortytwo.language.field.TypedVariable;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.GenericType;
import fortytwo.language.type.StructureType;
import fortytwo.vm.constructions.Structure;
import fortytwo.vm.environment.OrderedEnvironment;
import fortytwo.vm.environment.TypeEnvironment;
import fortytwo.vm.errors.DNEErrors;
import fortytwo.vm.errors.TypingErrors;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralObject;

/**
 * Represents a field assignment.
 */
public class ParsedFieldAssignment extends ParsedAssignment {
	/**
	 * The name of the variable whose field is being modified.
	 */
	public final Expression toModify;
	/**
	 * The field to be set.
	 */
	public final VariableIdentifier field;
	/**
	 * The value to set the field to.
	 */
	public final Expression value;
	public ParsedFieldAssignment(Expression name, VariableIdentifier field,
			Expression parseExpression, Context context) {
		super(context);
		this.toModify = name;
		this.field = field;
		this.value = parseExpression;
	}
	@Override
	public boolean typeCheck(TypeEnvironment env) {
		if (!(toModify.type(env) instanceof StructureType)) {
			// TODO handle when not fed a structure...
		}
		StructureType type = (StructureType) toModify.type(env);
		Structure struct = env.structs.getStructure(type);
		Optional<ConcreteType> potentialFieldType = struct.typeof(field);
		if (!potentialFieldType.isPresent()) DNEErrors.fieldDNE(struct, field);
		ConcreteType fieldType = potentialFieldType.get();
		if (!fieldType.equals(value.type(env)))
			TypingErrors.fieldAssignmentTypeMismatch(struct,
					new TypedVariable(field, fieldType), value, env);
		return true;
	}
	@Override
	public Optional<LiteralExpression> execute(OrderedEnvironment environment) {
		((LiteralObject) toModify.literalValue(environment)).redefine(field,
				value.literalValue(environment));
		return Optional.empty();
	}
	@Override
	public void clean(OrderedEnvironment environment) {
		// nothing to clean.
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(toModify, field, value);
	}
	@Override
	public boolean isSimple() {
		return true;
	}
	@Override
	public Optional<GenericType> returnType(TypeEnvironment env) {
		return Optional.empty();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (field == null ? 0 : field.hashCode());
		result = prime * result + (toModify == null ? 0 : toModify.hashCode());
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
		if (toModify == null) {
			if (other.toModify != null) return false;
		} else if (!toModify.equals(other.toModify)) return false;
		if (value == null) {
			if (other.value != null) return false;
		} else if (!value.equals(other.value)) return false;
		return true;
	}
}
