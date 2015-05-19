package fortytwo.compiler.parsed.constructions;

import java.util.List;
import java.util.stream.Collectors;

import fortytwo.compiler.parsed.declaration.FunctionDefinition;
import fortytwo.compiler.parsed.declaration.FunctionReturn;
import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.vm.constructions.FunctionImplemented;
import fortytwo.vm.environment.StaticEnvironment;

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
		return new FunctionImplemented(f, body.stream()
				.map(x -> x.contextualize(environment))
				.collect(Collectors.toList()), r.output == null ? null
				: r.output.contextualize(environment));
	}
}
