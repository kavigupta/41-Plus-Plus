package fortytwo.language.type;

import java.util.List;

public class Structure implements ConcreteType {
	public final List<String> name;
	public final List<ConcreteType> types;
	public Structure(List<String> name, List<ConcreteType> types) {
		this.name = name;
		this.types = types;
	}
}
