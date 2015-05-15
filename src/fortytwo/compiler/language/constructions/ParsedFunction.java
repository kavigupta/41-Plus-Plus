package fortytwo.compiler.language.constructions;

import java.util.List;

import fortytwo.compiler.language.declaration.FunctionDefinition;
import fortytwo.compiler.language.declaration.FunctionReturn;
import fortytwo.compiler.language.statements.ParsedStatement;

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
