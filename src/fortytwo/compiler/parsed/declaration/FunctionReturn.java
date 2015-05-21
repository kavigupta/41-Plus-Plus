package fortytwo.compiler.parsed.declaration;

import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.language.SourceCode;

public class FunctionReturn implements Declaration {
	public final ParsedExpression output;
	public FunctionReturn(ParsedExpression output) {
		this.output = output;
	}
	@Override
	public SentenceType type() {
		return SentenceType.FUNCTION_RETURN;
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this);
	}
	@Override
	public boolean isSimple() {
		return true;
	}
}
