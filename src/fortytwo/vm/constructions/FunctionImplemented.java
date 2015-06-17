package fortytwo.vm.constructions;

import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.declaration.FunctionDefinition;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.GenericType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypeWithoutContext;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.statements.Statement;

public class FunctionImplemented extends Function42 {
	public final FunctionDefinition f;
	public final List<Statement> body;
	public final Expression output;
	public FunctionImplemented(FunctionDefinition f, List<Statement> body,
			Expression output) {
		this.f = f;
		this.body = body;
		this.output = output;
	}
	@Override
	public LiteralExpression apply(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster) {
		LocalEnvironment local = env.minimalLocalEnvironment();
		for (int i = 0; i < f.parameterVariables.size(); i++) {
			local.vars.assign(f.parameterVariables.get(i), arguments.get(i));
		}
		body.stream().forEach(x -> x.execute(local));
		LiteralExpression out = output == null ? null : output
				.literalValue(local);
		body.stream().forEach(x -> x.clean(local));
		for (int i = 0; i < f.parameterVariables.size(); i++) {
			f.parameterVariables.stream().forEach(local.vars::deregister);
		}
		return out;
	}
	@Override
	public GenericType outputType() {
		return output == null ? new PrimitiveType(
				PrimitiveTypeWithoutContext.VOID, Context.SYNTHETIC)
				: output.resolveType();
	}
	@Override
	public FunctionSignature signature() {
		return FunctionSignature.getInstance(f.name, f.parameterTypes,
				f.outputType);
	}
}
