package fortytwo.vm.environment.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.FunctionType;

public abstract class AbstractTypeEnvironment {
	public static enum EnvironmentType {
		ORDERED, UNORDERED, FRAME
	}
	public static enum RequestType {
		ANY, REQUIRES_UNORDERED, ONLY_THIS_FRAME
	}
	public final TypeRoster typeRoster;
	public final EnvironmentType envType;
	private final List<FrameTypeEnvironment> frames;
	public AbstractTypeEnvironment(TypeRoster typeRoster,
			EnvironmentType isOrdered) {
		this.typeRoster = typeRoster;
		this.envType = isOrdered;
		this.frames = new ArrayList<>();
	}
	public void addType(VariableIdentifier variableIdentifier,
			ConcreteType concreteType) {
		typeRoster.addType(variableIdentifier, concreteType);
	}
	public void addFrame(FrameTypeEnvironment env) {
		frames.add(env);
	}
	public void putReference(VariableIdentifier id, FunctionType type) {
		typeRoster.putReference(id, type);
	}
	public Optional<ConcreteType> typeOf(VariableIdentifier name,
			RequestType request) {
		if (allowRequest(request)) {
			final Optional<ConcreteType> type = typeRoster.typeOf(name);
			if (type.isPresent()) return type;
		}
		if (request == RequestType.ONLY_THIS_FRAME) return Optional.empty();
		return checkParentForTypeOf(name);
	}
	public Optional<FunctionType> referenceTo(FunctionName name,
			List<ConcreteType> types, RequestType request) {
		if (allowRequest(request)) {
			final Optional<FunctionType> sig = typeRoster.referenceTo(name,
					types);
			if (sig.isPresent()) return sig;
			for (FrameTypeEnvironment frame : frames) {
				Optional<FunctionType> subsig = frame
						.getTypeOfMemberFunction(name, types);
				if (subsig.isPresent()) return subsig;
			}
		}
		if (request == RequestType.ONLY_THIS_FRAME) return Optional.empty();
		return checkParentForTypeOf(name, types);
	}
	protected abstract Optional<ConcreteType> checkParentForTypeOf(
			VariableIdentifier name);
	protected abstract Optional<FunctionType> checkParentForTypeOf(
			FunctionName name, List<ConcreteType> types);
	protected abstract boolean allowRequest(RequestType type);
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((typeRoster == null) ? 0 : typeRoster.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		AbstractTypeEnvironment other = (AbstractTypeEnvironment) obj;
		if (typeRoster == null) {
			if (other.typeRoster != null) return false;
		} else if (!typeRoster.equals(other.typeRoster)) return false;
		return true;
	}
}
