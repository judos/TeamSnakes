package controller;

import controller.game.PlayerControls;
import model.game.Map;
import model.game.Options;
import view.game.MapDrawer;

public interface GameI {
	public MapDrawer getMapDrawer();
	public Map getMap();
	public Options getOptions();
	public PlayerControls getControls();
}
