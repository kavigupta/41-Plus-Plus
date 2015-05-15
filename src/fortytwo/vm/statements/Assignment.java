package fortytwo.vm.statements;

import java.util.ArrayList;
import java.util.List;

import lib.standard.collections.Pair;
import fortytwo.compiler.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralObject;

public class Assignment implements Statement {
	public final VariableIdentifier name, field;
	public final Expression value;
	public Assignment(VariableIdentifier name, VariableIdentifier field,
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
		List<Pair<VariableIdentifier, LiteralExpression>> fields = new ArrayList<>(
				obj.fields);
		// TODO finish this after refactor
	}
}
