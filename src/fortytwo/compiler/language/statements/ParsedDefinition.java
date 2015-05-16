package fortytwo.compiler.language.statements;

import fortytwo.compiler.language.identifier.TypeIdentifier;
import fortytwo.compiler.language.identifier.VariableIdentifier;
import fortytwo.compiler.languages.constructions.ParsedVariableRoster;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.statements.Definition;
import fortytwo.vm.statements.Statement;

public class ParsedDefinition implements ParsedStatement {
	public final TypeIdentifier type;
	public final VariableIdentifier name;
	public final ParsedVariableRoster fields;
	public ParsedDefinition(TypeIdentifier type, VariableIdentifier name,
			ParsedVariableRoster fields) {
		this.type = type;
		this.name = name;
		this.fields = fields;
	}
	@Override
	public Statement contextualize(LocalEnvironment environment) {
		return new Definition(type, name, fields.contextualize(environment));
	}
	@Override
	public SentenceType type() {
		return SentenceType.DEFINITION;
	}
}
