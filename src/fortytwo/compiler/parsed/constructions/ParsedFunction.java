package fortytwo.compiler.parsed.constructions;

import java.util.List;

import fortytwo.compiler.parsed.declaration.FunctionDefinition;
import fortytwo.compiler.parsed.declaration.FunctionReturn;
import fortytwo.compiler.parsed.statements.ParsedStatement;

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
}
