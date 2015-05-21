package fortytwo.compiler.parsed.statements;

import fortytwo.compiler.parsed.constructions.ParsedVariableRoster;
import fortytwo.language.SourceCode;
import fortytwo.language.field.Field;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.statements.Definition;
import fortytwo.vm.statements.Statement;

public class ParsedDefinition implements ParsedStatement {
	public final Field name;
	public final ParsedVariableRoster fields;
	public ParsedDefinition(Field name, ParsedVariableRoster fields) {
		this.name = name;
		this.fields = fields;
	}
	@Override
	public Statement contextualize(StaticEnvironment environment) {
		return new Definition(name, fields.contextualize(environment),
				environment.structs);
	}
	@Override
	public SentenceType type() {
		return SentenceType.DEFINITION;
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this);
	}
	@Override
	public boolean isSimple() {
		return true;
	}
}
