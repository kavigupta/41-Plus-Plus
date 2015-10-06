package fortytwo.vm.environment;

import java.util.List;
import java.util.Optional;

import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.environment.type.AbstractTypeEnvironment;
import fortytwo.vm.environment.type.FrameTypeEnvironment;
import fortytwo.vm.expressions.LiteralFunction;

public class FrameEnvironment {
	private final OrderedEnvironment parent;
	private UnorderedEnvironment child;
	private final List<VariableIdentifier> allowed;
	public FrameEnvironment(OrderedEnvironment parent,
			List<VariableIdentifier> allowed) {
		this.parent = parent;
		this.allowed = allowed;
	}
	public void initializeChild(UnorderedEnvironment child) {
		this.child = child;
	}
	public AbstractTypeEnvironment typeEnvironment() {
		FrameTypeEnvironment fte = new FrameTypeEnvironment(
				parent.staticEnvironment(), allowed);
		fte.initializeChild(child.typeEnv);
		return fte;
	}
	public Optional<LiteralFunction> getFunction(FunctionSignature sig,
			List<ConcreteType> types) {
		return child.funcs.get(sig, types);
	}
}
