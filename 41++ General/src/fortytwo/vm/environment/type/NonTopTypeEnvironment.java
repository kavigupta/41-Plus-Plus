package fortytwo.vm.environment.type;

import java.util.List;

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
		return new NonTopTypeEnvironment(environment);
	}
	private NonTopTypeEnvironment(AbstractTypeEnvironment env) {
		super(new TypeRoster(env.typeRoster.structs,
				new FunctionSignatureRoster(), new VariableTypeRoster()));
		this.container = env;
	}
	@Override
	protected ConcreteType checkParentForTypeOf(VariableIdentifier name) {
		return container.typeOf(name);
	}
	@Override
	protected FunctionType checkParentForTypeOf(FunctionName name,
			List<ConcreteType> types) {
		return container.referenceTo(name, types);
	}
}
