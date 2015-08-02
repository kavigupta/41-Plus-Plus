package fortytwo.compiler.parsed;

import fortytwo.language.classification.SentenceType;

/**
 * An interface representing a Sentence.
 */
public interface Sentence extends ParsedConstruct {
	/**
	 * @return the type of this sentence
	 */
	public SentenceType kind();
}
