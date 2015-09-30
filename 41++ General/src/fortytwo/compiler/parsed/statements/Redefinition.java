package fortytwo.compiler.parsed.statements;

import java.util.Optional;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.field.TypedVariable;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.GenericType;
import fortytwo.vm.environment.OrderedEnvironment;
import fortytwo.vm.environment.type.AbstractTypeEnvironment;
import fortytwo.vm.errors.TypingErrors;
import fortytwo.vm.expressions.LiteralExpression;

/**
 * Represents a statement of the form
 * 
 * {@code Set the value of <variable> to <expression>.}
 */
public class Redefinition extends Statement {
	/**
	 * The variable identifier to modify
	 */
	private final VariableIdentifier name;
	/**
	 * The value to set the name to.
	 */
	private final Expression value;
	public Redefinition(VariableIdentifier name, Expression expr,
			Context context) {
		super(context);
		this.name = name;
		this.value = expr;
	}
	@Override
	public boolean typeCheck(AbstractTypeEnvironment env) {
		if (name.type(env).equals(value.type(env))) return true;
		TypingErrors.redefinitionTypeMismatch(
				new TypedVariable(name, name.type(env)), value, env);
		// should never get here
		return false;
	}
	@Override
	public Optional<LiteralExpression> execute(OrderedEnvironment environment) {
		environment.vars.redefine(name, value.literalValue(environment));
		return Optional.empty();
	}
	@Override
	public Optional<GenericType> returnType(AbstractTypeEnvironment env) {
		return Optional.empty();
	}
	@Override
	public void clean(OrderedEnvironment environment) {
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
	public final SentenceType kind() {
		return SentenceType.ASSIGNMENT;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (value == null ? 0 : value.hashCode());
		result = prime * result + (name == null ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final Redefinition other = (Redefinition) obj;
		if (value == null) {
			if (other.value != null) return false;
		} else if (!value.equals(other.value)) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		return true;
	}
}
