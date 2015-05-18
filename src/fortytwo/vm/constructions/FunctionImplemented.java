package fortytwo.vm.constructions;

import java.util.List;

import fortytwo.compiler.parsed.declaration.FunctionDefinition;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.VariableTypeRoster;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.statements.Definition;
import fortytwo.vm.statements.Statement;

public class FunctionImplemented implements Function42 {
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
			List<LiteralExpression> arguments) {
		LocalEnvironment local = env.minimalLocalEnvironment();
		for (int i = 0; i < f.parameterVariables.size(); i++) {
			local.vars.assign(f.parameterVariables.get(i), arguments.get(i));
		}
		body.stream().forEach(x -> x.execute(local));
		return output == null ? null : output.literalValue(local);
	}
	@Override
	public ConcreteType outputType(GlobalEnvironment env) {
		VariableTypeRoster roster = new VariableTypeRoster();
		for (int i = 0; i < f.parameterTypes.size(); i++)
			roster.add(f.parameterVariables.get(i), f.parameterTypes.get(i));
		body.forEach(s -> {
			if (s instanceof Definition) {
				roster.add(((Definition) s).toCreate);
			}
		});
		return output == null ? PrimitiveType.VOID : output.resolveType(
				roster, env.structs);
	}
}
