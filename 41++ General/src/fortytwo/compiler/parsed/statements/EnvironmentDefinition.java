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
import fortytwo.vm.environment.FrameEnvironment;
import fortytwo.vm.environment.OrderedEnvironment;
import fortytwo.vm.environment.UnorderedEnvironment;
import fortytwo.vm.environment.type.AbstractTypeEnvironment;
import fortytwo.vm.environment.type.FrameTypeEnvironment;
import fortytwo.vm.expressions.LiteralExpression;

public class EnvironmentDefinition extends Statement {
	private final List<VariableIdentifier> varIds;
	private final List<Sentence> sentences;
	private FrameEnvironment created = null;
	public EnvironmentDefinition(Context context,
			List<VariableIdentifier> varIds, List<Sentence> sentences) {
		super(context);
		this.varIds = varIds;
		this.sentences = sentences;
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
	protected boolean typeCheck(AbstractTypeEnvironment environment) {
		FrameTypeEnvironment env = new FrameTypeEnvironment(environment,
				varIds);
		AbstractTypeEnvironment child = UnorderedEnvironment
				.interpret(Optional.of(environment), sentences).typeEnv;
		env.initializeChild(child);
		environment.addFrame(env);
		return true;
	}
	@Override
	public Optional<LiteralExpression> execute(OrderedEnvironment environment) {
		// TODO
		created = new FrameEnvironment(environment, varIds);
		UnorderedEnvironment child = UnorderedEnvironment
				.interpret(Optional.of(created.typeEnvironment()), sentences);
		created.initializeChild(child);
		environment.frames.add(created);
		return Optional.empty();
	}
	@Override
	public void clean(OrderedEnvironment environment) {
		environment.frames.remove(created);
		created = null;
	}
	@Override
	public Optional<GenericType> returnType(AbstractTypeEnvironment env) {
		return Optional.empty();
	}
}
