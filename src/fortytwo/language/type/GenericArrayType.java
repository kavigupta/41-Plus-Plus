package fortytwo.language.type;

import fortytwo.vm.environment.TypeVariableRoster;

public class GenericArrayType implements GenericType {
	public final GenericType contents;
	public GenericArrayType(GenericType contents) {
		this.contents = contents;
	}
	@Override
	public Kind kind() {
		return Kind.CONSTRUCTOR;
	}
	@Override
	public TypeVariableRoster match(ConcreteType toMatch) {
		if (!(toMatch instanceof ArrayType))
			throw new RuntimeException(/* LOWPRI-E */);
		return contents.match(((ArrayType) toMatch).contentType);
	}
	@Override
	public ConcreteType resolve(TypeVariableRoster roster) {
		return new ArrayType(contents.resolve(roster));
	}
	@Override
	public String toSourceCode() {
		return "(array of " + contents.toSourceCode() + ")";
	}
}
