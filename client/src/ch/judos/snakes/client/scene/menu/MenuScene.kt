package ch.judos.snakes.client.scene.menu

import ch.judos.snakes.client.core.base.BasicScene
import ch.judos.snakes.client.core.base.Design
import ch.judos.snakes.client.core.base.SceneController
import ch.judos.snakes.client.core.io.InputController
import ch.judos.snakes.client.core.ui.*
import ch.judos.snakes.client.core.window.GameWindow
import ch.judos.snakes.client.model.GameData
import ch.judos.snakes.client.model.PlayerData
import java.awt.Dimension
import java.util.function.Consumer

class MenuScene(
		sceneController: SceneController,
		inputController: InputController,
		window: GameWindow,
		private val gameData: GameData
) : BasicScene(sceneController, inputController, window) {

	private var playerListener: Consumer<PlayerData>? = null
	private lateinit var playerList: SelectableList<String>
	private lateinit var lobbyList: SelectableList<String>

	init {
		initUI()
		listenToPlayerUpdates()
	}

	private fun listenToPlayerUpdates() {
		this.playerListener = Consumer {
			this.playerList.update(it.playerList)
		}
		this.gameData.playerData.subscribers.add(this.playerListener!!)
	}

	override fun unloadScene() {
		this.gameData.playerData.subscribers.remove(this.playerListener!!)
	}

	private fun initUI() {
		val panel = BasePanel()
		panel.add(Label("Welcome " + this.gameData.settings.name, true))
		panel.add(Spacer(1, 40))

		val lobbyPlayerPanel = BasePanel(isVertical = false)

		this.lobbyList = SelectableList(listOf<String>("Lobby1", "Lobby2"), inputController)
		this.lobbyList.setWeight(1, 0)
		val scrollLobby = ScrollPanel(Dimension(150, 300))
		scrollLobby.add(this.lobbyList)
		val lobbyPanel = BasePanel().add(Label("Lobbies:")).add(scrollLobby)
		lobbyPlayerPanel.add(lobbyPanel)
		lobbyPlayerPanel.add(Spacer(40, 1))

		this.playerList = SelectableList(gameData.playerData.playerList, inputController)
		this.playerList.setWeight(1, 0)
		val scrollPlayer = ScrollPanel(Dimension(150, 300))
		scrollPlayer.add(this.playerList)

		val playerPanel = BasePanel().add(Label("Players online:")).add(scrollPlayer)
		lobbyPlayerPanel.add(playerPanel)
		panel.add(lobbyPlayerPanel)
		panel.add(Spacer(1, 40))

		val buttonPanel = BasePanel(isVertical = false)
		buttonPanel.add(Button("Create New Game") {
			// TODO: implement
		})
		buttonPanel.add(Spacer(50, 1))
		buttonPanel.add(Button("Join Game") {
			// TODO:
		})
		buttonPanel.add(Spacer(50, 1))
		buttonPanel.add(Button("Quit") {
			this.sceneController.quit()
		})
		panel.add(buttonPanel)

		val view = WindowComponent(Design.titleFont)
		view.isHeadless = true
		view.isMovable = false
		view.addComponent(panel)
		this.ui.addWindow(view)
	}

}
