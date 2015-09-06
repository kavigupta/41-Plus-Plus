package fortytwo.vm.constructions;

import fortytwo.compiler.Context;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.GenericType;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralFunction;

public abstract class Function42 extends LiteralExpression {
	public FunctionName name;
	public LiteralFunction implementation;
	protected Function42(Context context) {
		super(context);
	}
	public abstract GenericType outputType();
	public abstract FunctionSignature signature();
}
