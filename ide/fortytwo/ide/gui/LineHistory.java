package fortytwo.ide.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import fortytwo.vm.errors.Error42;
import fortytwo.vm.expressions.LiteralExpression;

public class LineHistory extends JScrollPane {
	private JPanel entry;
	private int index = 0;
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
	public void put(JTextPane comp, boolean setForeground) {
		if (setForeground) comp.setForeground(Color.WHITE);
		String text = comp.getText();
		comp.setContentType("text/html");
		comp.setText(text);
		// comp.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
		comp.setBackground(Color.BLACK);
		comp.setOpaque(true);
		comp.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		comp.setEditable(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.weightx = 1.0;
		gbc.gridwidth = 10;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridy = index;
		index++;
		entry.add(comp, gbc);
	}
	public void displayCommand(String cmd) {
		cmd = cmd.replaceAll("[\\r\\n]", "");
		System.out.println("Command " + cmd);
		JTextPane command = new JTextPane();
		command.setText(">> " + cmd);
		put(command, true);
	}
	public void displayOutput(LiteralExpression literalValue) {
		JTextPane result = new JTextPane();
		result.setText("&nbsp;= " + literalValue.toSourceCode());
		put(result, true);
	}
	public void displayln(String line) {
		JTextPane result = new JTextPane();
		result.setText("   " + line);
		put(result, true);
	}
	public void displayerr(Error42 err) {
		StringBuffer error = new StringBuffer();
		error.append("<html>&nbsp;&nbsp;&nbsp;<font color=#ff0000>");
		boolean openTilde = true;
		for (int i = 0; i < err.msg.length(); i++) {
			if (err.msg.charAt(i) == '~') {
				if (openTilde)
					error.append("</font><font color=#00ff00>");
				else error.append("</font><font color=#ff0000>");
				openTilde = !openTilde;
			} else if (err.msg.charAt(i) == '{') {
				error.append("</font><font color=#0000ff>");
			} else if (err.msg.charAt(i) == '}') {
				error.append("</font><font color=#00ff00>");
			} else {
				error.append(err.msg.charAt(i));
			}
		}
		error.append("</font></html>");
		JTextPane pane = new JTextPane();
		pane.setText(error.toString());
		put(pane, false);
	}
}
