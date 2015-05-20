package fortytwo.language.type;

import fortytwo.vm.environment.TypeVariableRoster;

public interface GenericType {
	public Kind kind();
	public TypeVariableRoster match(ConcreteType toMatch);
	public ConcreteType resolve(TypeVariableRoster roster);
}
