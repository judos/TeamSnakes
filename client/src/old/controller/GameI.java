package old.controller;

import old.controller.game.PlayerControls;
import old.model.game.Map;
import old.model.game.Options;
import old.view.game.MapDrawer;

public interface GameI {
	public MapDrawer getMapDrawer();
	public Map getMap();
	public Options getOptions();
	public PlayerControls getControls();
}
