package fortytwo.ide.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import lib.standard.gui.TextEditor;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextArea;

import fortytwo.ide.environment.GUILinkedEnvironment;

public class Editor42 extends TextEditor {
	public static final double CONSOLE_START = .7, CONSOLE_END = .97;
	public RSyntaxTextArea textArea;
	public Console42 console;
	private GUILinkedEnvironment environ;
	public Editor42() {
		super();
		setTitle("41++ Editor");
		textArea = ComponentFactory.getEditorBox42();
		setTextArea(textArea);
		environ = new GUILinkedEnvironment(new LineHistory());
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
		// TODO Auto-generated method stub.
	}
	public void cmdAbout() {
		// TODO Auto-generated method stub.
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
			UIManager.setLookAndFeel(UIManager
					.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
			// do nothing here, just use the normal one...
		}
		Editor42 frame = new Editor42();
		frame.setVisible(true);
	}
}
