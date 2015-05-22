package fortytwo.vm.statements;

import fortytwo.language.field.Field;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralObject;

public class FieldAssignment implements Statement {
	public final VariableIdentifier name;
	public final Field field;
	public final Expression value;
	public FieldAssignment(VariableIdentifier name, Field field,
			Expression value) {
		this.name = name;
		this.field = field;
		this.value = value;
	}
	@Override
	public void execute(LocalEnvironment environment) {
		LiteralExpression expr = environment.vars.referenceTo(name);
		if (!(expr instanceof LiteralObject))
			throw new RuntimeException(/* LOWPRI-E */);
		LiteralObject obj = (LiteralObject) expr;
		obj.fields.redefine(field.name, value.literalValue(environment));
	}
	@Override
	public void clean(LocalEnvironment environment) {
		// nothing to clean.
	}
	@Override
	public boolean typeCheck() {
		if (!field.type.equals(value.resolveType()))
			throw new RuntimeException(/* LOWPRI-E */);
		return true;
	}
}
