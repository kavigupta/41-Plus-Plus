package fortytwo.ide.gui;

import static java.awt.event.InputEvent.CTRL_DOWN_MASK;
import static java.awt.event.InputEvent.SHIFT_DOWN_MASK;
import static java.awt.event.KeyEvent.*;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import fortytwo.utils.IOUtils;

public abstract class TextEditor extends JFrame {
	private static final long serialVersionUID = 1L;
	private final JScrollPane contentPane;
	protected final JMenu fileMenu, editMenu;
	private String path = null;
	private String lastSaved = "";
	public TextEditor() {
		setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				cmdClose();
			}
		});
		contentPane = new JScrollPane();
		contentPane.setHorizontalScrollBarPolicy(
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		fileMenu = new JMenu("File");
		editMenu = new JMenu("Edit");
		populateMenuBar();
	}
	protected void setTextArea(Component main) {
		contentPane.setViewportView(main);
	}
	private void populateMenuBar() {
		final JMenuBar bar = new JMenuBar();
		fileMenu.add(getMenuItem("New", VK_N, false, this::cmdNew));
		fileMenu.add(getMenuItem("Close", VK_W, false, e -> cmdClose()));
		fileMenu.add(getMenuItem("Open", VK_O, false, e -> cmdOpen()));
		fileMenu.add(getMenuItem("Save", VK_S, false, e -> cmdSave()));
		fileMenu.add(getMenuItem("Save As", VK_S, true, e -> cmdSaveAs()));
		fileMenu.add(
				getMenuItem("Select All", VK_A, false, getSelectAllAction()));
		bar.add(fileMenu);
		editMenu.add(getMenuItem("Undo", VK_Z, false, getUndoAction()));
		editMenu.add(getMenuItem("Redo", VK_Y, false, getRedoAction()));
		editMenu.add(getMenuItem("Cut", VK_X, false, getCutAction()));
		editMenu.add(getMenuItem("Copy", VK_C, false, getCopyAction()));
		editMenu.add(getMenuItem("Paste", VK_V, false, getPasteAction()));
		bar.add(editMenu);
		setJMenuBar(bar);
	}
	protected static JMenuItem getMenuItem(String name, int key, boolean shift,
			ActionListener listener) {
		final JMenuItem item = new JMenuItem(name);
		if (key != -1) {
			int mask = CTRL_DOWN_MASK;
			if (shift) mask |= SHIFT_DOWN_MASK;
			item.setAccelerator(KeyStroke.getKeyStroke(key, mask));
		}
		if (listener instanceof Action)
			item.setAction((Action) listener);
		else item.addActionListener(listener);
		return item;
	}
	protected abstract Action getCutAction();
	protected abstract Action getCopyAction();
	protected abstract Action getUndoAction();
	protected abstract Action getRedoAction();
	protected abstract Action getPasteAction();
	protected abstract Action getSelectAllAction();
	protected abstract void setText(String text);
	protected abstract String getText();
	public void cmdNew(ActionEvent e) {
		if (!saved()) if (!promptSave("create a new file")) return;
		path = null;
		setText("");
		lastSaved = "";
	}
	protected void cmdClose() {
		if (saved()) System.exit(0);
		if (promptSave("close the editor")) System.exit(0);
	}
	public void cmdOpen() {
		if (!saved()) if (!promptSave("open a new file")) return;
		this.path = chooseFile(true);
		if (path == null) return;
		try {
			setText(IOUtils.read(path));
			lastSaved = getText();
		} catch (final FileNotFoundException e) {
			displayError(String.format("The file \"%s\" was not found", path),
					e);
		} catch (final IOException e) {
			displayError(String.format("Error in reading from \"%s\"", path),
					e);
		}
	}
	public void cmdSave() {
		if (path == null) cmdSaveAs();
		try {
			IOUtils.write(path, getText());
			lastSaved = getText();
		} catch (final IOException e) {
			displayError(String.format("Error in writing to \"%s\"", path), e);
		}
	}
	public void cmdSaveAs() {
		path = chooseFile(false);
		if (path == null) return;
		cmdSave();
	}
	private boolean saved() {
		if (path == null) return getText().length() == 0;
		return lastSaved.equals(getText());
	}
	private boolean promptSave(String type) {
		final int response = JOptionPane.showOptionDialog(this,
				String.format("Do you want to save your changes before you %s?",
						type),
				"Save your changes?", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE, null,
				new String[] { "Yes", "No", "Cancel" }, "Yes");
		if (response == 2) return false;
		if (response == 1) return true;
		cmdSave();
		return true;
	}
	private void displayError(String msg, IOException exc) {
		JOptionPane.showMessageDialog(this, msg, msg,
				JOptionPane.ERROR_MESSAGE);
	}
	private String chooseFile(boolean open) {
		final JFileChooser choose = new JFileChooser(path);
		if (open)
			choose.showOpenDialog(this);
		else choose.showSaveDialog(this);
		return choose.getSelectedFile().getAbsolutePath();
	}
}
