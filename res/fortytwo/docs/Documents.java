package fortytwo.docs;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import lib.standard.io.IO;
import fortytwo.metadata.Metadata;

public class Documents {
	public static final String ABOUT_HTML;
	static {
		String aboutLocal;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				Documents.class.getResourceAsStream("about.html")))) {
			aboutLocal = Metadata.applyMacroes(IO.read(br));
		} catch (Throwable t) {
			aboutLocal = "About documentation not found.";
		}
		ABOUT_HTML = aboutLocal;
	}
}
