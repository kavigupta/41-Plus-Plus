package fortytwo.language.type;

public interface ConcreteType extends GenericType {
	@Override
	public default Kind kind() {
		return Kind.CONCRETE;
	}
}
