package fortytwo.library.standard;

import java.util.Arrays;
import java.util.List;

import fortytwo.language.field.Field;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.constructions.Structure;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.errors.CriticalErrors;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralObject;

public class FunctionFieldAccess extends Function42 {
	public final VariableIdentifier field;
	public final Structure from;
	public FunctionFieldAccess(VariableIdentifier field, Structure from) {
		this.field = field;
		this.from = from;
	}
	@Override
	public LiteralExpression apply(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster) {
		if (!(arguments.get(0) instanceof LiteralObject))
			CriticalErrors.fieldAccessOnNonObject(this, arguments);
		LiteralObject obj = (LiteralObject) arguments.get(0);
		return obj.valueOf(field);
	}
	@Override
	public ConcreteType outputType() {
		for (Field f : from.fields) {
			if (f.name.equals(field)) return f.type;
		}
		throw new RuntimeException(/**/);
	}
	@Override
	public FunctionSignature signature() {
		return FunctionSignature
				.getInstance(FunctionName.getInstance("the",
						field.name.token, "of", ""), Arrays
						.asList(from.type), outputType());
	}
}
