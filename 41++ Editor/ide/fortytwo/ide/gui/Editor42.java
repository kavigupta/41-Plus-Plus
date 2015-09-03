package fortytwo.ide.gui;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextArea;

import fortytwo.docs.Documents;
import fortytwo.icon.IconManager;
import fortytwo.ide.environment.GUILinkedEnvironment;
import fortytwo.metadata.Metadata;

public class Editor42 extends TextEditor {
	public static final double CONSOLE_START = .7, CONSOLE_END = .97;
	public RSyntaxTextArea textArea;
	public Console42 console;
	private GUILinkedEnvironment environ;
	public Editor42() {
		super();
		setTitle("41++ Editor");
		IconManager.setIcon(this);
		textArea = ComponentFactory.getEditorBox42();
		setTextArea(textArea);
		environ = new GUILinkedEnvironment(new LineHistory(), this::getText);
		JMenu help = new JMenu("Help");
		help.add(getMenuItem("Manual", -1, false, e -> this.cmdHelpManual()));
		help.add(getMenuItem("About", -1, false, e -> this.cmdAbout()));
		console = new Console42(this, environ);
		console.setVisible(true);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		console.setLocation(0, (int) (dim.height * CONSOLE_START));
		console.setSize(dim.width,
				(int) (dim.height * (CONSOLE_END - CONSOLE_START)));
		getJMenuBar().add(help);
	}
	public void cmdHelpManual() {
		// TODO Finish manual of help
	}
	public void cmdAbout() {
		JDialog about = new JDialog(this);
		JEditorPane jtp = new JEditorPane();
		jtp.setEditable(false);
		jtp.setContentType("text/html");
		jtp.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					if (Desktop.isDesktopSupported()) {
						try {
							Desktop.getDesktop().browse(e.getURL().toURI());
							return;
						} catch (IOException | URISyntaxException e1) {}
					}
					JOptionPane.showMessageDialog(Editor42.this,
							String.format(
									"Hyperlinks do not appear to be supported by your platform, you will have to manually navigate to \"%s\"",
									e.getURL()),
							"Manual Hyperlink Required",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		jtp.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		jtp.setText(Metadata.applyMacroes(Documents.ABOUT_HTML));
		about.setContentPane(jtp);
		about.setTitle("About 41++ Editor");
		about.setBounds(100, 100, 400, 400);
		about.setVisible(true);
	}
	@Override
	protected Action getCutAction() {
		return RSyntaxTextArea.getAction(RTextArea.CUT_ACTION);
	}
	@Override
	protected Action getCopyAction() {
		return RSyntaxTextArea.getAction(RTextArea.COPY_ACTION);
	}
	@Override
	protected Action getUndoAction() {
		return RSyntaxTextArea.getAction(RTextArea.UNDO_ACTION);
	}
	@Override
	protected Action getRedoAction() {
		return RSyntaxTextArea.getAction(RTextArea.REDO_ACTION);
	}
	@Override
	protected Action getPasteAction() {
		return RSyntaxTextArea.getAction(RTextArea.PASTE_ACTION);
	}
	@Override
	protected Action getSelectAllAction() {
		return RSyntaxTextArea.getAction(RTextArea.SELECT_ALL_ACTION);
	}
	@Override
	protected void setText(String text) {
		textArea.setText(text);
	}
	@Override
	protected String getText() {
		return textArea.getText();
	}
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
			// do nothing here, just use the normal one...
		}
		Editor42 frame = new Editor42();
		frame.setVisible(true);
	}
}
