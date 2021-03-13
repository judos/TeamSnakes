package ch.judos.snakes.client.core.ui;

import ch.judos.snakes.client.core.base.Design;
import ch.judos.snakes.client.core.io.InputAction;
import ch.judos.snakes.client.core.io.InputEvent;

import java.awt.*;
import java.util.function.Consumer;

public class WindowComponent extends BaseComponent {

	public String title = "";
	public boolean isMovable = true;
	public boolean isHeadless = false;
	public boolean isVisible = true;
	/**
	 * may be used& changed externally to manage rendering and input handling order
	 */
	public int zIndex = Integer.MAX_VALUE;

	/**
	 * will be called upon closing this window
	 */
	public Consumer<String> onClose;

	protected BasePanel content = (BasePanel) new BasePanel().setWeight(1, 0);

	protected int bannerHeight;
	protected int baselineDelta;
	protected boolean moving;
	protected Point movingMouseInitialPos;
	protected Font font;
	protected Point movingWindowInitialPos;
	protected boolean isDisposed;
	public boolean enabled = true;

	public WindowComponent(Font font) {
		super();
		this.font = font;

		Canvas c = new Canvas();
		FontMetrics fm = c.getFontMetrics(font);
		this.bannerHeight = 4 + fm.getAscent() + fm.getDescent();
		this.baselineDelta = 2 + fm.getAscent();
	}

	public void dispose(String closeResult) {
		this.zIndex = Integer.MIN_VALUE;
		this.isDisposed = true;
		if (this.onClose != null) {
			if (closeResult == null) closeResult = "";
			this.onClose.accept(closeResult);
		}
	}

	public BasePanel getContent() {
		return content;
	}

	public void dispose() {
		dispose("");
	}

	public <T extends Component> T addComponent(T c) {
		this.content.add(c);
		return c;
	}

	@Override
	public void render(Graphics2D g, Point mousePos) {
		if (!this.isVisible) {
			return;
		}
		if (this.moving) {
			updateChildPositionsWhileMoving(g, mousePos);
		}

		this.content.render(g, mousePos);
		if (!this.isHeadless) {
			g.setColor(Design.windowBannerBg);
			g.fillRect(this.pos.x, this.pos.y, this.size.width, this.bannerHeight);
		}
		g.setColor(Design.windowBorder);
		g.drawRect(this.pos.x, this.pos.y, this.size.width - 1, this.size.height - 1);
		if (!this.isHeadless) {
			g.setFont(this.font);
			g.setColor(Design.textColor);
			g.drawString(this.title, this.pos.x + Design.textMarginX, this.pos.y + this.baselineDelta);
		}
	}

	private void updateChildPositionsWhileMoving(Graphics g, Point mousePos) {
		Point delta = new Point(mousePos.x - movingMouseInitialPos.x, mousePos.y - movingMouseInitialPos.y);
		this.pos = new Point(this.movingWindowInitialPos);
		this.pos.translate(delta.x, delta.y);
		stayInsideClip(g);
		this.content.layout(this.pos.x, this.pos.y + bannerHeight, this.size.width, this.size.height - bannerHeight);
	}

	private void stayInsideClip(Graphics g) {
		Rectangle clip = g.getClipBounds();
		if (this.pos.x < clip.x) {
			this.pos.x = clip.x;
		}
		if (this.pos.y < clip.y) {
			this.pos.y = clip.y;
		}
		if (this.pos.x + this.size.width > clip.x + clip.width) {
			this.pos.x = clip.x + clip.width - this.size.width;
		}
		if (this.pos.y + this.size.height > clip.y + clip.height) {
			this.pos.y = clip.y + clip.height - this.size.height;
		}
	}

	@Override
	public void layout(int x, int y, int w, int h) {
		super.layout(x, y, w, h);
		if (this.isHeadless) {
			this.content.layout(x, y, w, h);
		} else {
			this.content.layout(x, y + bannerHeight, w, h - bannerHeight);
		}
	}

	@Override
	public void handleInput(InputEvent event) {
		if (!this.isVisible || !this.enabled) {
			return;
		}
		if (event.isAction(InputAction.SELECT)) {
			if (event.isPress() && isPointInside(event.getCurrentMousePosition())) {
				// pull on top, zIndex is later adjusted by DesktopComponent
				this.zIndex = Integer.MAX_VALUE / 2;
			}
			if (isInsideBanner(event.getCurrentMousePosition()) && event.isPress() && this.isMovable && !this.isHeadless) {
				this.moving = true;
				this.movingMouseInitialPos = event.getCurrentMousePosition();
				this.movingWindowInitialPos = new Point(this.pos);
				event.consume();
				return;
			}
			if (event.isReleased() && this.moving) {
				this.moving = false;
				event.consume();
				return;
			}
		}

		this.content.handleInput(event);

		// consume any select events inside this window and don't pass them to windows behind
		if (event.isAction(InputAction.SELECT) || event.isAction(InputAction.SELECT2)) {
			if (isPointInside(event.getCurrentMousePosition())) {
				event.consume();
			}
		}
	}

	public boolean isInsideBanner(Point pos) {
		if (this.isHeadless || !this.isVisible) return false;
		return pos.x >= this.pos.x && pos.y >= this.pos.y && pos.x <= this.pos.x + this.size.getWidth() && pos.y <= this.pos.y + this.bannerHeight;
	}

	@Override
	public boolean isPointInside(Point pos) {
		if (!this.isVisible) return false;
		return super.isPointInside(pos);
	}

	@Override
	public Dimension getPreferedDimension() {
		if (this.isHeadless) {
			return this.content.getPreferedDimension();
		}
		Dimension size = this.content.getPreferedDimension();
		size.height += bannerHeight; // top nav and border bottom
		int titleWidth = 2 * Design.textMarginX + measureTextSize(this.font, this.title).width;
		size.width = Math.max(titleWidth, size.width);
		return size;
	}

}
