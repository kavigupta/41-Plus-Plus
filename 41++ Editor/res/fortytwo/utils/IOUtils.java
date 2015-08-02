package fortytwo.utils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class IOUtils {
	public static String read(URI uri) throws IOException {
		return Files
				.readAllLines(Paths.get(uri))
				.stream()
				.collect(StringBuilder::new,
						(x, y) -> x.append(System.lineSeparator())
								.append(y),
						(x, y) -> new StringBuffer(x).append(
								System.lineSeparator()).append(y))
				.toString().substring(System.lineSeparator().length());
	}
	public static void write(String path, String text) throws IOException {
		Files.write(Paths.get(new File(path).toURI()), Arrays.asList(text));
	}
}
