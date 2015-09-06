package fortytwo.compiler.parsed.declaration;

import java.util.ArrayList;
import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.Sentence;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;

/**
 * Represents a function that has been parsed but whose suite sub-declarations
 * have not been fully parsed into a static environment.
 */
public class FunctionConstruct implements Sentence {
	/**
	 * The function definition
	 */
	public final FunctionDefinition declaration;
	/**
	 * The body of the function, in the form of a series of sentences
	 */
	public final List<Sentence> suite;
	public FunctionConstruct(FunctionDefinition declaration,
			List<Sentence> suite) {
		this.declaration = declaration;
		this.suite = suite;
	}
	@Override
	public boolean isSimple() {
		return false;
	}
	@Override
	public String toSourceCode() {
		return declaration.toSourceCode()
				+ (suite.size() == 0 ? "" : SourceCode.displaySeries(suite));
	}
	@Override
	public Context context() {
		final ArrayList<Sentence> s = new ArrayList<>(suite);
		suite.add(declaration);
		return Context.sum(s);
	}
	@Override
	public SentenceType kind() {
		return SentenceType.FUNCTION;
	}
	@Override
	public String toString() {
		return "FunctionConstruct [declaration=" + declaration.toSourceCode()
				+ ", suite=" + suite + "]";
	}
}
