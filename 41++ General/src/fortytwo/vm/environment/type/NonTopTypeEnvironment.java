package fortytwo.vm.environment.type;

import java.util.List;
import java.util.Optional;

import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.FunctionType;
import fortytwo.vm.environment.FunctionSignatureRoster;
import fortytwo.vm.environment.VariableTypeRoster;

public class NonTopTypeEnvironment extends AbstractTypeEnvironment {
	private final AbstractTypeEnvironment container;
	public static NonTopTypeEnvironment getChild(
			AbstractTypeEnvironment environment) {
		return new NonTopTypeEnvironment(environment, EnvironmentType.ORDERED);
	}
	private NonTopTypeEnvironment(AbstractTypeEnvironment env,
			EnvironmentType envType) {
		super(new TypeRoster(env.typeRoster.structs,
				new FunctionSignatureRoster(), new VariableTypeRoster()),
				envType);
		this.container = env;
	}
	@Override
	protected Optional<ConcreteType> checkParentForTypeOf(
			VariableIdentifier name) {
		return container.typeOf(name, RequestType.ANY);
	}
	@Override
	protected Optional<FunctionType> checkParentForTypeOf(FunctionName name,
			List<ConcreteType> types) {
		return container.referenceTo(name, types, RequestType.ANY);
	}
	@Override
	protected boolean allowRequest(RequestType type) {
		return type != RequestType.REQUIRES_UNORDERED;
	}
}
