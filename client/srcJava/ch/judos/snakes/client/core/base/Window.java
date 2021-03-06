package ch.judos.snakes.client.core.base;

import ch.judos.snakes.client.core.io.InputController;

import java.awt.*;


public interface Window {

	Dimension getScreenSize();

	InputController getInput();

}
