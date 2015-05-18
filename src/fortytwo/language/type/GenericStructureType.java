package fortytwo.language.type;

import java.util.List;

public class GenericStructureType implements GenericType {
	public final List<String> name;
	public final List<TypeVariable> typeVariables;
	public GenericStructureType(List<String> name,
			List<TypeVariable> typeVariables) {
		this.name = name;
		this.typeVariables = typeVariables;
	}
	@Override
	public Kind kind() {
		return Kind.CONSTRUCTOR;
	}
}
