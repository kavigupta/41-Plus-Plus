package fortytwo.compiler.parsed.constructions;

import java.util.List;
import java.util.stream.Collectors;

import fortytwo.compiler.parsed.declaration.FunctionDefinition;
import fortytwo.compiler.parsed.declaration.FunctionReturn;
import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.vm.constructions.FunctionImplemented;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.statements.Statement;

public class ParsedFunction {
	public final FunctionDefinition f;
	public final List<ParsedStatement> body;
	public final FunctionReturn r;
	public ParsedFunction(FunctionDefinition f, List<ParsedStatement> body,
			FunctionReturn r) {
		this.f = f;
		this.body = body;
		this.r = r;
	}
	public FunctionImplemented contextualize(StaticEnvironment environment) {
		f.addTypes(environment);
		List<Statement> bodyS = body.stream().map(x -> {
			return x.contextualize(environment);
		}).collect(Collectors.toList());
		return new FunctionImplemented(f, bodyS, r.output == null ? null
				: r.output.contextualize(environment));
	}
	public void decontextualize(StaticEnvironment env) {
		body.stream().forEach(x -> x.decontextualize(env));
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
