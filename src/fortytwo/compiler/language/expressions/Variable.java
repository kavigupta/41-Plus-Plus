package fortytwo.compiler.language.expressions;

public class Variable implements Expression {
	public final String contents;
	public static Variable getInstance(String contents) {
		return new Variable(contents);
	}
	public Variable(String contents) {
		this.contents = contents;
	}
}
