package fortytwo.compiler.language.declaration;

import java.util.ArrayList;

import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.identifier.FunctionName;
import fortytwo.vm.constructions.Field;

public class FunctionDefinition implements Declaration {
	public final FunctionName signature;
	public final ArrayList<Field> argTypes;
	public final ParsedExpression parsedExpression;
	public FunctionDefinition(FunctionName signature,
			ArrayList<Field> argTypes, ParsedExpression parsedExpression) {
		this.signature = signature;
		this.argTypes = argTypes;
		this.parsedExpression = parsedExpression;
	}
	@Override
	public SentenceType type() {
		return SentenceType.DECLARATION_FUNCT;
	}
}
