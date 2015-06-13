package fortytwo.icon;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import fortytwo.ide.gui.Editor42;

public class IconManager {
	private static final BufferedImage ICON_PROGRAM;
	static {
		BufferedImage iconProgram;
		try {
			iconProgram = ImageIO.read(IconManager.class
					.getResource("icon.png"));
		} catch (Throwable t) {
			iconProgram = null;
		}
		ICON_PROGRAM = iconProgram;
	}
	public static void setIcon(Editor42 ed) {
		if (ICON_PROGRAM != null) ed.setIconImage(ICON_PROGRAM);
	}
}
