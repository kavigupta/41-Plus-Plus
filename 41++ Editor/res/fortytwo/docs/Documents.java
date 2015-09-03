package fortytwo.docs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

import fortytwo.metadata.Metadata;
import fortytwo.utils.IOUtils;

public class Documents {
	public static final String ABOUT_HTML;
	static {
		String aboutLocal;
		try {
			aboutLocal = Metadata.applyMacroes(IOUtils.read(
					new BufferedReader(new InputStreamReader(Documents.class
							.getResourceAsStream("about.html")))));
		} catch (Throwable t) {
			aboutLocal = "About documentation not found.\n" + t.toString()
					+ "\n" + Arrays.asList(t.getStackTrace());
		}
		ABOUT_HTML = aboutLocal;
	}
}
