package fortytwo.compiler.language;

import java.util.List;
import java.util.function.Function;

public class Language {
	public static String articleized(String word) {
		if (isVowel(word.charAt(0)))
			return "an " + word;
		else return "a " + word;
	}
	public static boolean isVowel(char c) {
		c = Character.toLowerCase(c);
		return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
	}
	public static <T> String sayList(List<T> fields,
			Function<T, String> howToSay) {
		if (fields.size() == 0) return "";
		if (fields.size() == 1) return howToSay.apply(fields.get(0));
		StringBuffer sbuff = new StringBuffer();
		for (int i = 0; i < fields.size() - 1; i++)
			sbuff.append(howToSay.apply(fields.get(i))).append(", ");
		return sbuff.append("and ")
				.append(howToSay.apply(fields.get(fields.size() - 1)))
				.toString();
	}
}
