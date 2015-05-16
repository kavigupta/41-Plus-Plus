package fortytwo.compiler.language.identifier;

import fortytwo.compiler.language.Language;
import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.expressions.Expression;

public class TypeIdentifier implements ParsedExpression {
	// TODO some serious fixes are needed on this class. Perhaps interfaceify
	// it, split into three types
	// 1) Literal Types
	// 2) Type variables
	// 3) Structure Types
	public final String name;
	public static TypeIdentifier getInstance(String name) {
		if (Language.isValidVariableIdentifier(name))
			return new TypeIdentifier(name);
		if (!Language.isValidTypeIdentifier(name))
			throw new RuntimeException(/* LOWPRI-E */);
		return new TypeIdentifier(name);
	}
	private TypeIdentifier(String name) {
		this.name = name;
	}
	@Override
	public Expression contextualize(LocalEnvironment env) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public SentenceType type() {
		// TODO Auto-generated method stub
		return null;
	}
	// TODO make sure subclasses implement equals when push comes to shove
}
