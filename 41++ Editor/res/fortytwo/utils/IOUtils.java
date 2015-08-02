package fortytwo.utils;

import java.io.*;
import java.util.ArrayList;

public class IOUtils {
	public static String readLine(RandomAccessFile wiki) throws IOException {
		long start = wiki.getFilePointer();
		wiki.readLine();
		long end = wiki.getFilePointer();
		wiki.seek(start);
		byte[] b = new byte[(int) (end - start)];
		wiki.read(b);
		return new String(b).replace(System.lineSeparator(), "");
	}
	public static ArrayList<String> readLines(File file)
			throws FileNotFoundException, IOException {
		ArrayList<String> lines = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String ln;
			while ((ln = br.readLine()) != null)
				lines.add(ln);
		}
		return lines;
	}
	public static String read(String file) throws FileNotFoundException,
			IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			return read(br);
		}
	}
	public static String read(BufferedReader br) throws IOException {
		StringBuffer sbuff = new StringBuffer();
		String ln;
		while ((ln = br.readLine()) != null)
			sbuff.append(ln).append(NEWLINE);
		return sbuff.toString();
	}
	private static final String NEWLINE = System.lineSeparator();
	public static void writeLines(File file, Iterable<String> output)
			throws IOException {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
			for (String ln : output)
				bw.append(ln).append(NEWLINE);
		}
	}
	public static void write(String file, String text) throws IOException {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
			bw.append(text);
		}
	}
	public static String nameOf(String path) {
		File f = new File(path);
		String name = f.getName();
		if (!name.contains(".")) return name;
		return name.substring(0, name.lastIndexOf("."));
	}
}
