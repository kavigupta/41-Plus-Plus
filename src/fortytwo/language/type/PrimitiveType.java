package fortytwo.language.type;

public enum PrimitiveType implements ConcreteType {
	NUMBER, STRING, BOOLEAN, TYPE, VOID;
	public final String typeID() {
		return name().toLowerCase();
	}
}
