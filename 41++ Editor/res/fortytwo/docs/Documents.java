package fortytwo.docs;

import fortytwo.metadata.Metadata;
import fortytwo.utils.IOUtils;

public class Documents {
	public static final String ABOUT_HTML;
	static {
		String aboutLocal;
		try {
			aboutLocal = Metadata.applyMacroes(IOUtils.read(Documents.class
					.getResource("about.html").toURI()));
		} catch (Throwable t) {
			aboutLocal = "About documentation not found.";
		}
		ABOUT_HTML = aboutLocal;
	}
}
