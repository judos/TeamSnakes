package old.controller.game;

import ch.judos.generic.control.ActionCounter;
import ch.judos.generic.data.geometry.PointF;
import core.input.InputController;
import old.model.game.Snake;

public class PlayerControls {

	public static final int decreaseAtOnce = 1;
	public static final int decreaseEveryMs = 250;

	private Snake snake;
	private InputController inputController;
	private ActionCounter sizeDecrease;

	public PlayerControls(Snake snake, InputController mouseController) {
		this.snake = snake;
		this.inputController = mouseController;
		this.sizeDecrease = new ActionCounter(decreaseEveryMs);
	}

	public Snake getOwnSnake() {
		return this.snake;
	}

	public void update() {
		// speed up snake
//		if (this.inputController.isMouseButtonPressed(1)
//			&& this.snake.getSize() >= 10 + decreaseAtOnce) {
//			this.snake.speedupModifier = 2f;
//			if (this.sizeDecrease.action()) {
//				this.snake.changeSize(-decreaseAtOnce);
//			}
//		}
//		else {
//			this.snake.speedupModifier = 1f;
//		}

//		if (this.inputController.isKeyPressed(KeyEvent.VK_SPACE)) {
//			this.snake.changeSize(1);
//		}

		// turning snake
//		PointF mousePoint = new PointF(this.inputController.getMousePosition());
//		Dimension screenSize = this.inputController.getScreenSize();
//		PointF middleOfScreen = new PointF(screenSize.width / 2, screenSize.height / 2);
//		Angle targetAngle = mousePoint.getAAngleTo(middleOfScreen).turnClockwise(Angle.A_180);
//		this.snake.turnIntoDirection(targetAngle);
	}

	public PointF getFocusPoint() {
		return this.snake.getPoints().get(0);
	}

}
