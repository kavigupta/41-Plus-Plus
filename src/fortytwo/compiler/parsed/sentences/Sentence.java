package fortytwo.compiler.parsed.sentences;

import fortytwo.language.ParsedConstruct;
import fortytwo.language.classification.SentenceType;

public interface Sentence extends ParsedConstruct {
	public SentenceType type();
	@Override
	public String toSourceCode();
	public boolean isSimple();
}
