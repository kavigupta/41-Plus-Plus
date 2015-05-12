package fortytwo.compiler.language.expressions;

import fortytwo.vm.environment.Environment;
import fortytwo.vm.expressions.Expression;

public class ParsedVariable implements ParsedExpression {
	public final String name;
	public ParsedVariable(String name) {
		this.name = name;
	}
	@Override
	public Expression contextualize(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
}
