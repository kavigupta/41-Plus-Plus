package fortytwo.language.type;

import java.util.Optional;

import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.expressions.LiteralExpression;

public interface ConcreteType extends GenericType {
	@Override
	public default Kind kind() {
		return Kind.CONCRETE;
	}
	@Override
	public default Optional<TypeVariableRoster> match(ConcreteType toMatch) {
		if (toMatch.equals(this)) return Optional.of(new TypeVariableRoster());
		return Optional.empty();
	}
	@Override
	public default ConcreteType resolve(TypeVariableRoster roster) {
		return this;
	}
	@Override
	public String toSourceCode();
	public LiteralExpression defaultValue();
	@Override
	public default boolean isSimple() {
		return true;
	}
}
