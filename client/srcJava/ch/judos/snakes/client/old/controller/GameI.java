package ch.judos.snakes.client.old.controller;

import ch.judos.snakes.client.old.controller.game.PlayerControls;
import ch.judos.snakes.client.old.model.game.Map;
import ch.judos.snakes.client.old.model.game.Options;
import ch.judos.snakes.client.old.view.game.MapDrawer;

public interface GameI {
	public MapDrawer getMapDrawer();
	public Map getMap();
	public Options getOptions();
	public PlayerControls getControls();
}
