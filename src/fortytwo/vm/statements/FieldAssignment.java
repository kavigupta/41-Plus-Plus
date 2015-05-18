package fortytwo.vm.statements;

import fortytwo.language.field.Field;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StructureRoster;
import fortytwo.vm.environment.VariableTypeRoster;
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
		obj.fields.redefine(field.name, value);
	}
	@Override
	public boolean typeCheck(VariableTypeRoster typeRoster,
			StructureRoster structRoster) {
		if (!field.type.equals(value.resolveType(typeRoster, structRoster)))
			throw new RuntimeException(/* LOWPRI-E */);
		return true;
	}
}
