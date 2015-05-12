package fortytwo.compiler.language.expressions.calc;

import org.omg.CORBA.Environment;

import fortytwo.compiler.language.expressions.Expression;

public class Addition implements Expression {
	public final Expression first, second;
	public Addition(Expression first, Expression second,
			Environment environment) {
		this.first = first;
		this.second = second;
	}
	public static Expression subtraction(Expression first, Expression second) {
		;
		return new Addition(first, new Negation(second));
	}
}
