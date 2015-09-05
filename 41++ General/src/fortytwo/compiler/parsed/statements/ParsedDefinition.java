package fortytwo.compiler.parsed.statements;

import java.util.Optional;

import fortytwo.compiler.Context;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.field.TypedVariable;
import fortytwo.language.type.GenericType;
import fortytwo.vm.constructions.VariableRoster;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.environment.StructureRoster;
import fortytwo.vm.expressions.LiteralExpression;

/**
 * Represents statements that create new variables.
 */
public class ParsedDefinition extends ParsedStatement {
	/**
	 * The name and type of the variable to create.
	 */
	public final TypedVariable toCreate;
	/**
	 * The fields the variable will contain.
	 */
	public final VariableRoster<?> fields;
	/**
	 * @param name
	 *        The name and type of the variable to create.
	 * @param fields
	 *        The fields the variable will contain.
	 * @param context
	 *        The context of the sentence
	 */
	public ParsedDefinition(TypedVariable name, VariableRoster<?> fields,
			Context context) {
		super(context);
		this.toCreate = name;
		this.fields = fields;
	}
	@Override
	public boolean typeCheck(StaticEnvironment environment) {
		environment.addType(toCreate.name, toCreate.type);
		return environment.structs.typeCheckConstructor(environment, toCreate,
				fields, context());
	}
	@Override
	public Optional<LiteralExpression> execute(LocalEnvironment environment) {
		final StructureRoster struct = environment.global.staticEnv.structs;
		environment.vars.assign(toCreate.name, struct.instance(toCreate,
				fields.literalValue(environment), toCreate.name.context()));
		return Optional.empty();
	}
	@Override
	public void clean(LocalEnvironment environment) {
		environment.vars.deregister(toCreate.name);
	}
	@Override
	public SentenceType kind() {
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
	public Optional<GenericType> returnType(StaticEnvironment env) {
		return Optional.empty();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (fields == null ? 0 : fields.hashCode());
		result = prime * result + (toCreate == null ? 0 : toCreate.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final ParsedDefinition other = (ParsedDefinition) obj;
		if (fields == null) {
			if (other.fields != null) return false;
		} else if (!fields.equals(other.fields)) return false;
		if (toCreate == null) {
			if (other.toCreate != null) return false;
		} else if (!toCreate.equals(other.toCreate)) return false;
		return true;
	}
}
