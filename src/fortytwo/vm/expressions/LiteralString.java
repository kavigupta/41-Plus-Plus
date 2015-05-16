package fortytwo.vm.expressions;

import fortytwo.compiler.language.identifier.TypeIdentifier;
import fortytwo.vm.environment.VariableTypeRoster;

public class LiteralString extends LiteralExpression {
	public final String contents;
	public static LiteralString getInstance(String contents) {
		return new LiteralString(contents);
	}
	public LiteralString(String contents) {
		this.contents = contents;
	}
	@Override
	public TypeIdentifier resolveType(VariableTypeRoster typeRoster) {
		return TypeIdentifier.getInstance("string");
	}
}
