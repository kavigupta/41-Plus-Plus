package fortytwo.vm.statements;

import fortytwo.compiler.Context;
import fortytwo.language.SourceCode;
import fortytwo.language.field.Field;
import fortytwo.language.type.StructureType;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.errors.TypingErrors;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralObject;

public class FieldAssignment implements Statement {
	public final Expression obj;
	public final Field field;
	public final Expression value;
	private final Context context;
	public FieldAssignment(Expression obj, Field field, Expression value,
			Context context) {
		this.obj = obj;
		this.field = field;
		this.value = value;
		this.context = context;
	}
	@Override
	public void execute(LocalEnvironment environment) {
		if (!(obj.resolveType() instanceof StructureType)) {
			// should never happen.
			return;
		}
		((LiteralObject) obj.literalValue(environment)).redefine(field.name,
				value.literalValue(environment));
	}
	@Override
	public void clean(LocalEnvironment environment) {
		// nothing to clean.
	}
	@Override
	public boolean typeCheck() {
		if (!field.type.equals(value.resolveType()))
			TypingErrors.fieldAssignmentTypeMismatch(
					((LiteralObject) obj).struct, field, value);
		return true;
	}
	@Override
	public boolean isSimple() {
		return true;
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(obj, field.name, value);
	}
	@Override
	public Context context() {
		return context;
	}
}
