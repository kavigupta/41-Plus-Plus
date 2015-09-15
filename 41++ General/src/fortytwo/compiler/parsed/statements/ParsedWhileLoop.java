package fortytwo.compiler.parsed.statements;

import java.util.Optional;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.type.GenericType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypeWOC;
import fortytwo.vm.environment.OrderedEnvironment;
import fortytwo.vm.environment.TypeEnvironment;
import fortytwo.vm.errors.TypingErrors;
import fortytwo.vm.expressions.LiteralBool;
import fortytwo.vm.expressions.LiteralExpression;

public class ParsedWhileLoop extends ParsedStatement {
	public final Expression condition;
	public final ParsedStatement statement;
	public ParsedWhileLoop(Expression condition,
			ParsedStatement ParsedStatement, Context context) {
		super(context);
		this.condition = condition;
		this.statement = ParsedStatement;
	}
	@Override
	public boolean typeCheck(TypeEnvironment env) {
		if (!condition.type(env)
				.equals(new PrimitiveType(PrimitiveTypeWOC.BOOL,
						Context.SYNTHETIC)))
			TypingErrors.expectedBoolInCondition(false, condition, env);
		return statement.isTypeChecked(env);
	}
	@Override
	public Optional<LiteralExpression> execute(OrderedEnvironment environment) {
		while (((LiteralBool) condition.literalValue(environment)).contents) {
			final Optional<LiteralExpression> expr = statement
					.execute(environment);
			if (expr.isPresent()) return expr;
			statement.clean(environment);
		}
		return Optional.empty();
	}
	@Override
	public Optional<GenericType> returnType(TypeEnvironment env) {
		return statement.returnType(env);
	}
	@Override
	public void clean(OrderedEnvironment environment) {
		// forms a closure, no need to clean
	}
	@Override
	public SentenceType kind() {
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (condition == null ? 0 : condition.hashCode());
		result = prime * result
				+ (statement == null ? 0 : statement.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final ParsedWhileLoop other = (ParsedWhileLoop) obj;
		if (condition == null) {
			if (other.condition != null) return false;
		} else if (!condition.equals(other.condition)) return false;
		if (statement == null) {
			if (other.statement != null) return false;
		} else if (!statement.equals(other.statement)) return false;
		return true;
	}
}
