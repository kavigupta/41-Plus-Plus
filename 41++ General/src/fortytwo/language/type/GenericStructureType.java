package fortytwo.language.type;

import java.util.ArrayList;
import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;
import fortytwo.language.SourceCode;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.errors.TypingErrors;

public class GenericStructureType implements GenericType {
	public final List<LiteralToken> name;
	public final List<GenericType> inputs;
	private final Context context;
	public GenericStructureType(List<LiteralToken> structName,
			List<GenericType> inputs, Context context) {
		this.name = structName;
		this.inputs = inputs;
		this.context = context;
	}
	@Override
	public TypeVariableRoster match(ConcreteType toMatch) {
		if (!(toMatch instanceof StructureType)) return null;
		StructureType type = (StructureType) toMatch;
		if (type.types.size() != inputs.size()) return null;
		TypeVariableRoster roster = new TypeVariableRoster();
		for (int i = 0; i < type.types.size(); i++) {
			// this doesn't worry about reassigning an existing variable for
			// obvious reasons.
			TypeVariableRoster thisone = inputs.get(i).match(type.types.get(i));
			if (thisone == null) return null;
			roster.pairs.putAll(thisone.pairs);
		}
		return roster;
	}
	@Override
	public ConcreteType resolve(TypeVariableRoster roster) {
		List<ConcreteType> types = new ArrayList<>();
		for (GenericType gt : inputs) {
			ConcreteType typeParameter = gt.resolve(roster);
			TypingErrors.inresolubleType(gt);
			types.add(typeParameter);
		}
		return new StructureType(name, types, context);
	}
	@Override
	public Kind kind() {
		return Kind.CONSTRUCTOR;
	}
	@Override
	public Context context() {
		return context;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((inputs == null) ? 0 : inputs.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		GenericStructureType other = (GenericStructureType) obj;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		if (inputs == null) {
			if (other.inputs != null) return false;
		} else if (!inputs.equals(other.inputs)) return false;
		return true;
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this);
	}
	@Override
	public String toString() {
		return toSourceCode();
	}
}
