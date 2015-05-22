package fortytwo.language.identifier.functioncomponent;

public class FunctionArgument implements FunctionComponent {
	public static final FunctionArgument INSTANCE = new FunctionArgument();
	private FunctionArgument() {}
	@Override
	public String toString() {
		return "<arg>";
	}
	@Override
	public int hashCode() {
		return 0;
	}
	@Override
	public boolean equals(Object obj) {
		return true;
	}
}
