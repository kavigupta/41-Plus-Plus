package fortytwo.vm.environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.environment.type.NonTopTypeEnvironment;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralFunction;

public class OrderedEnvironment {
	private final UnorderedEnvironment container;
	public final List<FrameEnvironment> frames;
	private final VariableRoster<LiteralExpression> vars;
	public OrderedEnvironment(UnorderedEnvironment global) {
		this.container = global;
		frames = new ArrayList<>();
		vars = new VariableRoster<>();
	}
	public OrderedEnvironment reinitialize(
			UnorderedEnvironment newEnvironment) {
		final OrderedEnvironment newlocal = new OrderedEnvironment(
				newEnvironment);
		vars.pairs.forEach((k, v) -> newlocal.vars.assign(k, v));
		return newlocal;
	}
	public LiteralExpression referenceTo(VariableIdentifier id) {
		final LiteralExpression localE = vars.referenceTo(id);
		if (localE != null) return localE;
		final LiteralExpression globalE = container.referenceTo(id);
		return globalE;
	}
	public NonTopTypeEnvironment staticEnvironment() {
		final NonTopTypeEnvironment env = NonTopTypeEnvironment
				.getChild(container.typeEnv);
		vars.pairs
				.forEach((name, expr) -> env.addType(name, expr.resolveType()));
		return env;
	}
	public void assign(VariableIdentifier id, LiteralExpression value) {
		vars.assign(id, value);
	}
	public void redefine(VariableIdentifier name, LiteralExpression express) {
		vars.redefine(name, express);
	}
	public void deregister(VariableIdentifier name) {
		vars.deregister(name);
	}
	public Optional<LiteralFunction> getFunction(FunctionSignature sig,
			List<ConcreteType> types) {
		for (FrameEnvironment fe : frames) {
			Optional<LiteralFunction> inFrame = fe.getFunction(sig, types);
			if (inFrame.isPresent()) return inFrame;
		}
		return container.funcs.get(sig, types);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (container == null ? 0 : container.hashCode());
		result = prime * result + (vars == null ? 0 : vars.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final OrderedEnvironment other = (OrderedEnvironment) obj;
		if (container == null) {
			if (other.container != null) return false;
		} else if (!container.equals(other.container)) return false;
		if (vars == null) {
			if (other.vars != null) return false;
		} else if (!vars.equals(other.vars)) return false;
		return true;
	}
}
