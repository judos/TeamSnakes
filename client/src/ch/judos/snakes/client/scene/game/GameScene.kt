package ch.judos.snakes.client.scene.game

import ch.judos.snakes.client.old.controller.Game
import ch.judos.snakes.client.core.base.Scene
import ch.judos.snakes.client.core.base.SceneController
import ch.judos.snakes.client.core.io.InputEvent
import ch.judos.snakes.client.old.model.game.MapGenerator
import ch.judos.snakes.client.old.Assets
import ch.judos.snakes.client.old.view.Gui
import java.util.*

class GameScene(sceneController: SceneController) : Scene(sceneController) {

	init {
		val map = MapGenerator.getMap()
		val shutdown = Optional.of(Runnable { System.exit(0) })
		val gui = Gui(shutdown)

		val game = Game(map, gui)
		Assets.waitUntilAssetsAreLoaded()
		game.start()
	}

	override fun handleInput(event: InputEvent?) {
		TODO("Not yet implemented")
	}


}
