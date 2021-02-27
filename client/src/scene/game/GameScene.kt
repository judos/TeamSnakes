package scene.game

import controller.Game
import core.base.Scene
import core.base.SceneController
import core.input.InputEvent
import model.game.MapGenerator
import old.Assets
import view.Gui
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
