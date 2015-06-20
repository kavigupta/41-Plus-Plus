package fortytwo.compiler.parsed.statements;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.constructions.ParsedVariableRoster;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.field.Field;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.environment.StructureRoster;

public class ParsedDefinition implements ParsedStatement {
	public final Field toCreate;
	public final ParsedVariableRoster<?> fields;
	private final Context context;
	public ParsedDefinition(Field name, ParsedVariableRoster<?> fields,
			Context context) {
		this.toCreate = name;
		this.fields = fields;
		this.context = context;
	}
	@Override
	public boolean typeCheck(StaticEnvironment environment) {
		environment.addType(toCreate.name, toCreate.type);
		return environment.structs.typeCheckConstructor(environment,
				toCreate, fields, context);
	}
	@Override
	public void execute(LocalEnvironment environment) {
		StructureRoster struct = environment.global.staticEnv.structs;
		environment.vars.assign(toCreate.name, struct.instance(toCreate,
				fields.literalValue(environment), toCreate.name.context()));
	}
	@Override
	public void clean(LocalEnvironment environment) {
		environment.vars.deregister(toCreate.name);
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
	@Override
	public Context context() {
		return context;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fields == null) ? 0 : fields.hashCode());
		result = prime * result
				+ ((toCreate == null) ? 0 : toCreate.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ParsedDefinition other = (ParsedDefinition) obj;
		if (fields == null) {
			if (other.fields != null) return false;
		} else if (!fields.equals(other.fields)) return false;
		if (toCreate == null) {
			if (other.toCreate != null) return false;
		} else if (!toCreate.equals(other.toCreate)) return false;
		return true;
	}
}
