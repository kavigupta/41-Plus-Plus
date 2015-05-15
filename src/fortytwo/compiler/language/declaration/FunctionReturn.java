package fortytwo.compiler.language.declaration;

import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.statements.ParsedStatement;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.statements.Statement;

public class FunctionReturn implements ParsedStatement {
	public final ParsedExpression output;
	public FunctionReturn(ParsedExpression output) {
		this.output = output;
	}
	@Override
	public Statement contextualize(LocalEnvironment environment) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public SentenceType type() {
		return SentenceType.FUNCTION_RETURN;
	}
}
