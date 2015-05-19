package fortytwo.language.type;

public class GenericArrayType implements GenericType {
	public static final GenericType INSTANCE = new GenericArrayType();
	private GenericArrayType() {}
	@Override
	public Kind kind() {
		return Kind.CONSTRUCTOR;
	}
}
