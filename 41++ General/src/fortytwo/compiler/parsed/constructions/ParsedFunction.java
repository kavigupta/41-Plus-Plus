package fortytwo.compiler.parsed.constructions;

import java.util.List;
import java.util.stream.Collectors;

import fortytwo.compiler.parsed.declaration.FunctionDefinition;
import fortytwo.compiler.parsed.declaration.FunctionOutput;
import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.vm.constructions.FunctionImplemented;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.statements.Statement;

/**
 * A class representing a function that has been parsed from 41++ source
 */
public class ParsedFunction {
	/**
	 * The function definition
	 */
	private final FunctionDefinition f;
	/**
	 * The function body, which is composed of statements.
	 */
	private final List<ParsedStatement> body;
	/**
	 * The function output, which can be evaluated against the environment
	 * after the statements are executed. TODO allow multiple function output
	 * statements, simply allow first "unnested" one to end the function
	 */
	private final FunctionOutput r;
	/**
	 * Simple struct constructor
	 */
	public ParsedFunction(FunctionDefinition f, List<ParsedStatement> body,
			FunctionOutput r) {
		this.f = f;
		this.body = body;
		this.r = r;
	}
	/**
	 * Contextualizes this function into one that can actually be used (linking
	 * to other functions is done here)
	 * 
	 * @param environment the environment against which to contextualize this
	 * @return an implemented function representing this function
	 */
	public FunctionImplemented contextualize(StaticEnvironment environment) {
		StaticEnvironment local = StaticEnvironment.getChild(environment);
		f.registerParameters(local);
		List<Statement> bodyS = body.stream()
				.map(x -> x.contextualize(local))
				.collect(Collectors.toList());
		return new FunctionImplemented(f, bodyS, r.output == null ? null
				: r.output.contextualize(local));
	}
	public boolean typeCheck(StaticEnvironment env) {
		StaticEnvironment local = StaticEnvironment.getChild(env);
		f.registerParameters(local);
		body.forEach(x -> x.typeCheck(local));
		return true;
	}
	/**
	 * @return the definition of this function
	 */
	public FunctionDefinition definition() {
		return f;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((f == null) ? 0 : f.hashCode());
		result = prime * result + ((r == null) ? 0 : r.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ParsedFunction other = (ParsedFunction) obj;
		if (body == null) {
			if (other.body != null) return false;
		} else if (!body.equals(other.body)) return false;
		if (f == null) {
			if (other.f != null) return false;
		} else if (!f.equals(other.f)) return false;
		if (r == null) {
			if (other.r != null) return false;
		} else if (!r.equals(other.r)) return false;
		return true;
	}
}
