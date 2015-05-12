package fortytwo.compiler.language.expressions;

import fortytwo.vm.environment.Environment;
import fortytwo.vm.expressions.Expression;

public class Variable implements ParsedExpression {
	public final String name;
	public static Variable initialize(String name, Environment environment) {
		return environment.initialize(name);
	}
	public static Variable getReferenceTo(String name, Environment environment) {
		return environment.getReferenceTo(name);
	}
	public Variable(String name) {
		this.name = name;
	}
	@Override
	public Expression contextualize(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
}
