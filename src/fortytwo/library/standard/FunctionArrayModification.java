package fortytwo.library.standard;

import java.util.Arrays;
import java.util.List;

import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.GenericArrayType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.TypeVariable;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.expressions.LiteralArray;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralNumber;

public class FunctionArrayModification extends Function42 {
	public static final FunctionArrayModification ST = new FunctionArrayModification(
			"st");
	public static final FunctionArrayModification ND = new FunctionArrayModification(
			"nd");
	public static final FunctionArrayModification RD = new FunctionArrayModification(
			"rd");
	public static final FunctionArrayModification TH = new FunctionArrayModification(
			"th");
	private final String suffix;
	private FunctionArrayModification(String suffix) {
		this.suffix = suffix;
	}
	@Override
	public LiteralExpression apply(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster) {
		LiteralArray array = (LiteralArray) arguments.get(1);
		LiteralNumber index = (LiteralNumber) arguments.get(0);
		LiteralExpression value = arguments.get(2);
		array.set(index.contents.intValue(), value);
		return null;
	}
	@Override
	public PrimitiveType outputType() {
		return PrimitiveType.VOID;
	}
	@Override
	public FunctionSignature signature() {
		TypeVariable contents = new TypeVariable(
				VariableIdentifier.getInstance("_contents"));
		return FunctionSignature.getInstance(FunctionName.getInstance("Set",
				"the", "", suffix, "element", "of", "", "to", ""), Arrays
				.asList(PrimitiveType.NUMBER,
						new GenericArrayType(contents), contents),
				outputType());
	}
}
