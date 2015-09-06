package fortytwo.ide.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.apache.commons.lang3.StringEscapeUtils;

import fortytwo.compiler.parser.Tokenizer;
import fortytwo.vm.errors.Error42;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralString;

public class LineHistory extends JScrollPane {
	public static final String PROMPT_COLOR = "white";
	public static final String PRINT_COLOR = "white";
	public static final String ERROR_COLOR = "red";
	public static final String ERROR_NAME_COLOR = "green";
	public static final String ERROR_HIGHLIGHT_COLOR = "#8888FF";
	public static final String RESULT_COLOR = "yellow";
	private JPanel entry;
	private int index = 0;
	private ArrayList<String> history = new ArrayList<String>();
	public LineHistory() {
		super();
		entry = new JPanel();
		entry.setBackground(Color.BLACK);
		entry.setOpaque(true);
		entry.setLayout(new GridBagLayout());
		setBackground(Color.BLACK);
		setOpaque(true);
		this.setViewportView(entry);
	}
	public void put(JTextPane textBox, String color) {
		String text = textBox.getText();
		textBox.setContentType("text/html");
		text = String.format(
				"<font face=\"monospace\" color = \"%s\">%s</font>", color,
				text);
		textBox.setText(text);
		// comp.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
		textBox.setBackground(Color.BLACK);
		textBox.setOpaque(true);
		textBox.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		textBox.setEditable(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.weightx = 1.0;
		gbc.gridwidth = 10;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridy = index;
		index++;
		entry.add(textBox, gbc);
	}
	public void displayCommand(String cmd) {
		cmd = process(cmd);
		JTextPane command = new JTextPane();
		command.setText(">> " + cmd);
		put(command, PROMPT_COLOR);
	}
	public static String process(String cmd) {
		int i = cmd.length() - 1;
		while (cmd.charAt(i) == '\r' || cmd.charAt(i) == '\n')
			i--;
		return StringEscapeUtils.escapeHtml3(cmd.substring(0, i + 1))
				.replaceAll("\n", "<br>&nbsp;&nbsp;&nbsp;");
	}
	public void displayOutput(LiteralExpression literalValue) {
		String output = process(literalValue.toSourceCode());
		String store = literalValue.toSourceCode();
		if (literalValue instanceof LiteralString) {
			store = "'"
					+ Tokenizer.escape(store.substring(1, store.length() - 1))
					+ "'";
		}
		history.add(store);
		JTextPane result = new JTextPane();
		result.setText("&nbsp;= " + output);
		put(result, RESULT_COLOR);
	}
	public void displayln(String line) {
		JTextPane result = new JTextPane();
		result.setText("   " + line);
		put(result, PRINT_COLOR);
	}
	public void displayerr(Error42 err) {
		StringBuffer error = new StringBuffer();
		error.append(
				"<html><font face = \"monospace\">&nbsp;&nbsp;&nbsp;<font color=#ff0000>");
		boolean openTilde = true;
		String msg = process(err.msg);
		for (int i = 0; i < msg.length(); i++) {
			if (msg.charAt(i) == '~') {
				if (openTilde)
					error.append(String.format("</font><font color=\"%s\">",
							ERROR_HIGHLIGHT_COLOR));
				else error.append(String.format("</font><font color=\"%s\">",
						ERROR_COLOR));
				openTilde = !openTilde;
			} else if (msg.charAt(i) == '{') {
				error.append(String.format("</font><font color=\"%s\">",
						ERROR_HIGHLIGHT_COLOR));
			} else if (msg.charAt(i) == '}') {
				error.append(String.format("</font><font color=\"%s\">",
						ERROR_NAME_COLOR));
			} else {
				error.append(msg.charAt(i));
			}
		}
		error.append("</font></font></html>");
		JTextPane pane = new JTextPane();
		pane.setText(error.toString());
		put(pane, "");
	}
	public int nCommands() {
		return history.size();
	}
	public void set(int pointer, String text) {
		history.set(pointer, text);
	}
	public String get(int pointer) {
		return history.get(pointer);
	}
}
