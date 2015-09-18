package fortytwo.icon;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import fortytwo.ide.gui.Editor42;

public class IconManager {
	private static final BufferedImage ICON_PROGRAM;
	public static final String ICON_URL = IconManager.class
			.getResource("icon.png").toString();
	static {
		BufferedImage iconProgram;
		try {
			iconProgram = ImageIO
					.read(IconManager.class.getResourceAsStream("icon.png"));
		} catch (Throwable t) {
			iconProgram = null;
		}
		ICON_PROGRAM = iconProgram;
		// TODO Fix why this isn't working in the jar
	}
	public static void setIcon(Editor42 ed) {
		if (ICON_PROGRAM != null) ed.setIconImage(ICON_PROGRAM);
	}
}
