package fortytwo.compiler.parsed.statements;

import static fortytwo.language.Resources.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.Sentence;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.GenericType;
import fortytwo.vm.environment.OrderedEnvironment;
import fortytwo.vm.environment.TypeEnvironment;
import fortytwo.vm.expressions.LiteralExpression;

public class EnvironmentDefinition extends Statement {
	private final List<VariableIdentifier> varIds;
	private final List<Sentence> environment;
	public EnvironmentDefinition(Context context,
			List<VariableIdentifier> varIds, List<Sentence> environment) {
		super(context);
		this.varIds = varIds;
		this.environment = environment;
	}
	@Override
	public SentenceType kind() {
		return SentenceType.DEFINITION_ENVIRONMENT;
	}
	@Override
	public boolean isSimple() {
		return false;
	}
	@Override
	public String toSourceCode() {
		return DEFINE + STD_SEP + AN + STD_SEP + DECL_ENVIRON + WITH + ACCESS
				+ TO
				+ SourceCode.displayList(
						varIds.stream().map(VariableIdentifier::toSourceCode)
								.collect(Collectors.toList()));
	}
	@Override
	protected boolean typeCheck(TypeEnvironment environment) {
		// TODO
		return true;
	}
	@Override
	public Optional<LiteralExpression> execute(OrderedEnvironment environment) {
		// TODO
		return Optional.empty();
	}
	@Override
	public void clean(OrderedEnvironment environment) {
		// TODO
	}
	@Override
	public Optional<GenericType> returnType(TypeEnvironment env) {
		return Optional.empty();
	}
}
