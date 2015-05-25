package fortytwo.library.standard;

import java.util.Arrays;
import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.GenericArrayType;
import fortytwo.language.type.GenericType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.TypeVariable;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.expressions.LiteralArray;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralNumber;

public class FunctionArrayAccess extends Function42 {
	private static final TypeVariable CONTENTS = new TypeVariable(
			VariableIdentifier.getInstance(new Token("_contents", Context
					.synthetic())));
	public static final FunctionArrayAccess ST = new FunctionArrayAccess("st");
	public static final FunctionArrayAccess ND = new FunctionArrayAccess("nd");
	public static final FunctionArrayAccess RD = new FunctionArrayAccess("rd");
	public static final FunctionArrayAccess TH = new FunctionArrayAccess("th");
	private final String suffix;
	private FunctionArrayAccess(String suffix) {
		this.suffix = suffix;
	}
	@Override
	public LiteralExpression apply(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster) {
		LiteralArray array = (LiteralArray) arguments.get(1);
		LiteralNumber index = (LiteralNumber) arguments.get(0);
		return array.get(index.contents.intValue());
	}
	@Override
	public GenericType outputType() {
		return CONTENTS;
	}
	@Override
	public FunctionSignature signature() {
		return FunctionSignature.getInstance(StdLib42
				.functArrayAccess(suffix), Arrays.asList(
				PrimitiveType.NUMBER, new GenericArrayType(CONTENTS)),
				CONTENTS);
	}
}
