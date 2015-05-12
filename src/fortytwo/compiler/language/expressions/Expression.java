package fortytwo.compiler.language.expressions;

import fortytwo.compiler.language.statements.Statement;
import fortytwo.vm.environment.Environment;

public interface Expression extends Statement {
	public String type(Environment environment);
}
