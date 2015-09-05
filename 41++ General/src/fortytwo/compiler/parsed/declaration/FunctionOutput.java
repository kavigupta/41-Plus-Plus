package fortytwo.compiler.parsed.declaration;

import java.util.Optional;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.type.GenericType;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.expressions.LiteralExpression;

/**
 * A ParsedExpression wrapped up with a context so it can function as a
 * sentence.
 */
public class FunctionOutput extends ParsedStatement {
	/**
	 * The output, in the form of an Expression
	 */
	public final Expression output;
	/**
	 * @param output
	 *        the output of the function
	 * @param context
	 *        the context of the sentence
	 */
	public FunctionOutput(Expression output, Context context) {
		super(context);
		this.output = output;
	}
	@Override
	protected boolean typeCheck(StaticEnvironment environment) {
		return output.isTypeChecked(environment);
	}
	@Override
	public Optional<LiteralExpression> execute(LocalEnvironment environment) {
		return Optional.of(output.literalValue(environment));
	}
	@Override
	public void clean(LocalEnvironment environment) {
		// no-op
	}
	@Override
	public boolean isSimple() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public SentenceType kind() {
		return SentenceType.FUNCTION_OUTPUT;
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this);
	}
	@Override
	public Optional<GenericType> returnType(StaticEnvironment env) {
		return Optional.of(output.type(env));
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
		FunctionOutput other = (FunctionOutput) obj;
		if (output == null) {
			if (other.output != null) return false;
		} else if (!output.equals(other.output)) return false;
		return true;
	}
}
