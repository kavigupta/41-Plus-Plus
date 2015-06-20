package fortytwo.compiler.parsed.statements;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypeWithoutContext;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.errors.TypingErrors;
import fortytwo.vm.expressions.LiteralBool;

public class ParsedWhileLoop implements ParsedStatement {
	public final ParsedExpression condition;
	public final ParsedStatement statement;
	private final Context context;
	public ParsedWhileLoop(ParsedExpression condition,
			ParsedStatement ParsedStatement, Context context) {
		this.condition = condition;
		this.statement = ParsedStatement;
		this.context = context;
	}
	@Override
	public boolean typeCheck(StaticEnvironment env) {
		condition.typeCheck(env);
		if (!condition.resolveType(env).equals(
				new PrimitiveType(PrimitiveTypeWithoutContext.BOOL,
						Context.SYNTHETIC)))
			TypingErrors.expectedBoolInCondition(false, condition, env);
		return statement.typeCheck(env);
	}
	@Override
	public void execute(LocalEnvironment environment) {
		while (((LiteralBool) condition.literalValue(environment)).contents) {
			statement.execute(environment);
			statement.clean(environment);
		}
	}
	@Override
	public void clean(LocalEnvironment environment) {
		// forms a closure, no need to clean
	}
	@Override
	public SentenceType type() {
		return SentenceType.CONTROL_FLOW;
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this);
	}
	@Override
	public boolean isSimple() {
		return false;
	}
	@Override
	public Context context() {
		return context;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((condition == null) ? 0 : condition.hashCode());
		result = prime * result
				+ ((statement == null) ? 0 : statement.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ParsedWhileLoop other = (ParsedWhileLoop) obj;
		if (condition == null) {
			if (other.condition != null) return false;
		} else if (!condition.equals(other.condition)) return false;
		if (statement == null) {
			if (other.statement != null) return false;
		} else if (!statement.equals(other.statement)) return false;
		return true;
	}
}
