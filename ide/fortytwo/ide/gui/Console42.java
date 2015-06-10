package fortytwo.ide.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.border.EmptyBorder;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class Console42 extends JFrame {
	private JPanel contentPane;
	private RSyntaxTextArea line;
	private LineHistory history;
	public static void main(String[] args) {
		Console42 frame = new Console42();
		frame.setVisible(true);
	}
	public Console42() {
		setUndecorated(true);
		getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		setBounds(100, 100, 450, 300);
		// TODO fix bounds
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		line = ComponentFactory.getLine42(true, () -> {
			history.addCommand(line.getText());
			line.setText("");
		});
		contentPane.add(line, BorderLayout.SOUTH);
		history = new LineHistory();
		contentPane.add(history, BorderLayout.CENTER);
	}
}
