package fortytwo.language.type;

public enum PrimitiveType implements ConcreteType {
	NUMBER, STRING, BOOL, TYPE, VOID;
	public final String typeID() {
		return name().toLowerCase();
	}
}
