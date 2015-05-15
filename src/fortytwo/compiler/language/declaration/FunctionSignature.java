package fortytwo.compiler.language.declaration;

import java.util.List;

public class FunctionSignature {
	public final List<FunctionComponent> function;
	public static FunctionSignature getInstance(
			List<FunctionComponent> function) {
		return new FunctionSignature(function);
	}
	private FunctionSignature(List<FunctionComponent> function) {
		this.function = function;
	}
}
