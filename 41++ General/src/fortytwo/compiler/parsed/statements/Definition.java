package fortytwo.compiler.parsed.statements;

import static fortytwo.language.Resources.CALLED;
import static fortytwo.language.Resources.DEFINE;
import static fortytwo.language.Resources.WITH;

import java.util.Optional;

import fortytwo.compiler.Context;
import fortytwo.language.Language;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.field.TypedVariable;
import fortytwo.language.type.GenericType;
import fortytwo.vm.environment.OrderedEnvironment;
import fortytwo.vm.environment.StructureRoster;
import fortytwo.vm.environment.VariableRoster;
import fortytwo.vm.environment.type.AbstractTypeEnvironment;
import fortytwo.vm.expressions.LiteralExpression;

/**
 * Represents statements that create new variables.
 */
public class Definition extends Statement {
	/**
	 * The name and type of the variable to create.
	 */
	private final TypedVariable toCreate;
	/**
	 * The fields the variable will contain.
	 */
	private final VariableRoster<?> fields;
	/**
	 * @param name
	 *        The name and type of the variable to create.
	 * @param fields
	 *        The fields the variable will contain.
	 * @param context
	 *        The context of the sentence
	 */
	public Definition(TypedVariable name, VariableRoster<?> fields,
			Context context) {
		super(context);
		this.toCreate = name;
		this.fields = fields;
	}
	@Override
	public boolean typeCheck(AbstractTypeEnvironment environment) {
		environment.addType(toCreate.name, toCreate.type);
		return environment.typeRoster.structs.typeCheckConstructor(environment,
				toCreate, fields, context());
	}
	@Override
	public Optional<LiteralExpression> execute(OrderedEnvironment environment) {
		final StructureRoster struct = environment
				.staticEnvironment().typeRoster.structs;
		environment.assign(toCreate.name, struct.instance(toCreate,
				fields.literalValue(environment), toCreate.name.context()));
		return Optional.empty();
	}
	@Override
	public void clean(OrderedEnvironment environment) {
		environment.deregister(toCreate.name);
	}
	@Override
	public SentenceType kind() {
		return SentenceType.DEFINITION;
	}
	@Override
	public String toSourceCode() {
		return DEFINE + ' ' + Language.articleized(toCreate.type.toSourceCode())
				+ ' ' + CALLED + ' ' + toCreate.name.name
				+ (fields.numberOfVariables() == 0 ? ""
						: ' ' + WITH + ' '
								+ SourceCode.displayFieldList(fields));
	}
	@Override
	public boolean isSimple() {
		return true;
	}
	@Override
	public Optional<GenericType> returnType(AbstractTypeEnvironment env) {
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
		final Definition other = (Definition) obj;
		if (fields == null) {
			if (other.fields != null) return false;
		} else if (!fields.equals(other.fields)) return false;
		if (toCreate == null) {
			if (other.toCreate != null) return false;
		} else if (!toCreate.equals(other.toCreate)) return false;
		return true;
	}
}
