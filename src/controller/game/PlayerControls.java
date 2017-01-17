package controller.game;

import java.awt.Dimension;

import model.game.Snake;
import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.PointF;
import controller.MouseController;

public class PlayerControls {

	private Snake snake;
	private MouseController inputController;

	public PlayerControls(Snake snake, MouseController mouseController) {
		this.snake = snake;
		this.inputController = mouseController;
	}

	public void update() {
		PointF mousePoint = new PointF(this.inputController.getMousePosition());
		Dimension screenSize = this.inputController.getScreenSize();
		PointF middleOfScreen = new PointF(screenSize.width / 2, screenSize.height / 2);
		Angle targetAngle = mousePoint.getAAngleTo(middleOfScreen).turnClockwise(Angle.A_180);
		this.snake.turnIntoDirection(targetAngle);
	}

	public PointF getFocusPoint() {
		return this.snake.getPoints().get(0);
	}

}
