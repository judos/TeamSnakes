package ch.judos.snakes.client.scene.menu

import ch.judos.snakes.client.controller.GameController
import ch.judos.snakes.client.core.base.BasicScene
import ch.judos.snakes.client.core.base.Design
import ch.judos.snakes.client.core.base.SceneController
import ch.judos.snakes.client.core.io.InputController
import ch.judos.snakes.client.core.ui.*
import ch.judos.snakes.client.core.ui.LayoutPositioning.PositionH
import ch.judos.snakes.client.core.ui.LayoutPositioning.PositionV
import ch.judos.snakes.client.core.window.GameWindow
import ch.judos.snakes.client.model.GameData
import ch.judos.snakes.client.model.PlayerData
import ch.judos.snakes.common.model.Lobby
import java.awt.Dimension
import java.util.function.Consumer

class MenuScene(
		sceneController: SceneController,
		inputController: InputController,
		window: GameWindow,
		private val gameData: GameData,
		private val gameController: GameController
) : BasicScene(sceneController, inputController, window) {

	private lateinit var joinGameButton: Button
	private var playerListener: Consumer<PlayerData>? = null
	private lateinit var playerList: SelectableList<String>
	private lateinit var lobbyList: SelectableList<Lobby>

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

	private fun joinGame() {

	}

	private fun createGame() {
		this.gameController.createLobby()
		val dialog = WindowComponent(Design.textFont)
		dialog.title = "Creating Lobby..."
		this.ui.addWindow(dialog)
	}

	private fun initUI() {
		val panel = BasePanel()
		panel.padding = 40
		panel.margin = 40
		panel.add(Label("Welcome " + this.gameData.settings.name, true))

		val lobbyPlayerPanel = BasePanel(isVertical = false)

		this.lobbyList = SelectableList(gameData.lobbyData.lobbyList, inputController) {
			this.joinGameButton.setEnabled(it != null)
		}
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

		val buttonPanel = BasePanel(isVertical = false)
		buttonPanel.add(Button("Create New Game", this::createGame))
		buttonPanel.add(Spacer(50, 1))
		this.joinGameButton = Button("Join Game", this::joinGame)
		this.joinGameButton.setEnabled(false)
		buttonPanel.add(this.joinGameButton)
		buttonPanel.add(Spacer(50, 1))
		buttonPanel.add(Button("Quit") {
			this.sceneController.quit()
		}.setColored(Button.Colored.CAUTION))
		panel.add(buttonPanel)

		val view = WindowComponent(Design.titleFont)
		view.isHeadless = true
		view.isMovable = false
		view.addComponent(panel)
		this.ui.addWindow(view)
	}

}
