package fortytwo.compiler.language.identifier;

import java.util.List;

import fortytwo.compiler.language.identifier.functioncomponent.FunctionComponent;

public class FunctionName {
	public final List<FunctionComponent> function;
	public static FunctionName getInstance(
			List<FunctionComponent> function) {
		return new FunctionName(function);
	}
	private FunctionName(List<FunctionComponent> function) {
		this.function = function;
	}
}
