package fortytwo.compiler.language.expressions;

import fortytwo.vm.environment.Environment;

public class Variable implements Expression {
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
	public String type(Environment environment) {
		return environment.typeOf(this);
	}
	@Override
	public LiteralExpression evaluate(Environment environment) {
		return environment.valueOf(this);
	}
}
