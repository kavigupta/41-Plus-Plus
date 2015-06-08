package fortytwo.compiler.parsed.declaration;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;

public class FunctionReturn implements Declaration {
	public final ParsedExpression output;
	private final Context context;
	public FunctionReturn(ParsedExpression output, Context context) {
		this.output = output;
		this.context = context;
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
	@Override
	public Context context() {
		return context;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((output == null) ? 0 : output.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		FunctionReturn other = (FunctionReturn) obj;
		if (output == null) {
			if (other.output != null) return false;
		} else if (!output.equals(other.output)) return false;
		return true;
	}
}
