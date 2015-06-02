package fortytwo.language.type;

import fortytwo.language.ParsedConstruct;
import fortytwo.vm.environment.TypeVariableRoster;

public interface GenericType extends ParsedConstruct {
	public Kind kind();
	public TypeVariableRoster match(ConcreteType toMatch);
	public ConcreteType resolve(TypeVariableRoster roster);
	@Override
	public String toSourceCode();
	@Override
	public default boolean isSimple() {
		return true;
	}
}
