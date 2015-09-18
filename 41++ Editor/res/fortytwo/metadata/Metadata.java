package fortytwo.metadata;

import java.util.Arrays;
import java.util.function.Function;

import fortytwo.icon.IconManager;

public class Metadata {
	public static enum Macro implements Function<String, String> {
		VERSION("1.0"), LICENSE("Apache Licence 2.0"),
		LICENSE_URL("http://www.apache.org/licenses/LICENSE-2.0"),
		SYNTAX_HIGHLIGHTER("RSyntaxTextArea"),
		SYNTAX_HIGHLIGHTER_URL("http://bobbylight.github.io/RSyntaxTextArea/"),
		SYNTAX_HIGHLIGHTER_LICENCE_URL(
				"https://github.com/bobbylight/RSyntaxTextArea/blob/master/src/main/dist/RSyntaxTextArea.License.txt"),
		ICON_URL(IconManager.ICON_URL);
		public final String value;
		private Macro(String value) {
			this.value = value;
		}
		@Override
		public String apply(String t) {
			return t.replace(String.format("~%s~", this.toString()), value);
		}
	}
	public static String applyMacroes(String aboutLocal) {
		return Arrays.asList(Macro.values()).stream()
				.map(x -> (Function<String, String>) x)
				.reduce(x -> x, (x, y) -> x.compose(y)).apply(aboutLocal);
	}
}
