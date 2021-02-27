package core.base;

import java.awt.*;

public class Design {

	public static final Color panelBackground;
	public static final Color grayBorder;
	public static final Color textColor;

	public static final Color buttonBackground;
	public static final Color buttonBackgroundCaution;
	public static final Color buttonHover;
	public static final Color buttonHoverCaution;
	public static final Color buttonBorder;

	public static final Color textFieldBg;
	public static final Color textFieldBorder;
	public static final Color textFieldFocus;
	public static final Color textFieldBorderFocus;

	public static final Font textFont;
	public static final Font titleFont;

	public static final int toolbarHeight;
	public static final int buttonTextMarginX;
	public static final int buttonTextMarginY;
	public static final Color checkboxSelected;
	public static final Color checkboxNotSelected;
	public static final Color menuBlackOverlay;
	public static final Color textColorDisabled;
	public static final Color buttonDisabled;
	public static final Color windowBorder;
	public static final Color windowBannerBg;


	static {
		toolbarHeight = 220;

		grayBorder = Color.decode("#808080");
		textColor = Color.decode("#000000");
		textColorDisabled = Color.decode("#888888");

		panelBackground = Color.decode("#FFFFFE");
		windowBorder = Color.decode("#808080");
		windowBannerBg = Color.decode("F0F0FF");

		buttonTextMarginX = 10;
		buttonTextMarginY = 10;
		buttonBackground = Color.decode("#D0D0D0");
		buttonBackgroundCaution = Color.decode("#D0B0B0");
		buttonHover = Color.decode("#E6E6E6");
		buttonHoverCaution = Color.decode("#FFD0D0");
		buttonDisabled = Color.decode("#B0B0B0");
		buttonBorder = Color.decode("#808080");

		checkboxSelected = Color.decode("#AAEEAA");
		checkboxNotSelected = Color.decode("#EEAAAA");

		textFieldBg = Color.decode("#E0E0E0");
		textFieldBorder = Color.decode("#808080");
		textFieldFocus = Color.decode("#FFFFFF");
		textFieldBorderFocus = Color.decode("#8080FF");

		menuBlackOverlay = Color.decode("#000000A0");

		textFont = new Font("Arial", Font.PLAIN, 18);
		titleFont = new Font("Arial", Font.PLAIN, 36);
	}

}
