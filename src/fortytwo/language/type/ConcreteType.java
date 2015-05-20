package fortytwo.language.type;

import fortytwo.vm.environment.TypeVariableRoster;

public interface ConcreteType extends GenericType {
	@Override
	public default Kind kind() {
		return Kind.CONCRETE;
	}
	@Override
	public default TypeVariableRoster match(ConcreteType toMatch) {
		if (toMatch.equals(this)) return new TypeVariableRoster();
		throw new RuntimeException(/* LOWPRI-E */);
	}
	@Override
	public default ConcreteType resolve(TypeVariableRoster roster) {
		return this;
	}
	@Override
	public String toSourceCode();
}
