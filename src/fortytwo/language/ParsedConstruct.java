package fortytwo.language;

import fortytwo.compiler.Context;

public interface ParsedConstruct {
	public String toSourceCode();
	public boolean isSimple();
	public Context context();
}
