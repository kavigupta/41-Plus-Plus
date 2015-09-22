package fortytwo.compiler.parsed.declaration;

import java.util.Optional;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.compiler.parsed.statements.Statement;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.type.GenericType;
import fortytwo.vm.environment.OrderedEnvironment;
import fortytwo.vm.environment.TypeEnvironment;
import fortytwo.vm.expressions.LiteralExpression;

/**
 * A ParsedExpression wrapped up with a context so it can function as a
 * sentence representing a return statement. This is the only primitive
 * statement (i.e., one that does not call other statements) which returns
 * something other than {@code Optional.empty()} to
 */
public class FunctionOutput extends Statement {
	/**
	 * The output, in the form of an Expression
	 */
	private final Expression output;
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
	protected boolean typeCheck(TypeEnvironment environment) {
		return output.isTypeChecked(environment);
	}
	@Override
	public Optional<LiteralExpression> execute(OrderedEnvironment environment) {
		return Optional.of(output.literalValue(environment));
	}
	@Override
	public void clean(OrderedEnvironment environment) {
		// no-op
	}
	@Override
	public boolean isSimple() {
		return true;
	}
	@Override
	public SentenceType kind() {
		return SentenceType.FUNCTION_OUTPUT;
	}
	@Override
	public String toSourceCode() {
		String retVal = output.toSourceCode();
		if (retVal.length() != 0) retVal = " and output " + retVal;
		return "Exit the function" + retVal;
	}
	@Override
	public Optional<GenericType> returnType(TypeEnvironment env) {
		return Optional.of(output.type(env));
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (output == null ? 0 : output.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final FunctionOutput other = (FunctionOutput) obj;
		if (output == null) {
			if (other.output != null) return false;
		} else if (!output.equals(other.output)) return false;
		return true;
	}
}
