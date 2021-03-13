package ch.judos.snakes.client.scene.menu

import ch.judos.snakes.client.controller.GameController
import ch.judos.snakes.client.core.base.BasicScene
import ch.judos.snakes.client.core.base.Design
import ch.judos.snakes.client.core.base.SceneController
import ch.judos.snakes.client.core.io.InputController
import ch.judos.snakes.client.core.ui.*
import ch.judos.snakes.client.core.window.GameWindow
import ch.judos.snakes.client.model.GameData
import ch.judos.snakes.client.model.LobbyData
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

	private lateinit var mainWindow: WindowComponent
	private var lobbyListener: Consumer<LobbyData>? = null
	private lateinit var joinGameButton: Button
	private var playerListener: Consumer<PlayerData>? = null
	private lateinit var playerList: SelectableList<String>
	private lateinit var lobbyList: SelectableList<Lobby>

	init {
		initUI()
		listenToUpdates()
	}

	private fun listenToUpdates() {
		this.playerListener = Consumer {
			this.playerList.update(it.playerList)
		}
		this.lobbyListener = Consumer {
			this.lobbyList.update(it.lobbyList)
		}
		this.gameData.playerData.subscribers.add(this.playerListener!!)
		this.gameData.lobbyData.subscribers.add(this.lobbyListener!!)
	}

	override fun unloadScene() {
		this.gameData.playerData.subscribers.remove(this.playerListener!!)
		this.gameData.lobbyData.subscribers.remove(this.lobbyListener!!)
	}

	private fun joinGame(lobby: Lobby) {
		// TODO: implement
	}


	private fun createGame() {
		this.mainWindow.enabled = false
		val dialog = WindowComponent(Design.titleFont)
		dialog.title = "Loading"
		dialog.addComponent(BasePanel().apply { margin = 20; add(Label("Lobby is being created...")) })
		this.gameController.createLobby { (lobby, msg) ->
			if (lobby != null) {
				this.joinGame(lobby)
			} else {
				val dialog = WindowComponent(Design.titleFont).apply { title = "Error" }
				dialog.addComponent(BasePanel().apply { margin = 20; add(Label(msg ?: "unknown")) })
				this.ui.addWindow(dialog)
				this.inputController.schedule(3000) { dialog.dispose() }
			}
			dialog.dispose()
			this.mainWindow.enabled = true
		}
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
		this.joinGameButton = Button("Join Game") {
			this.lobbyList.selected?.let { this.joinGame(it) }
		}
		this.joinGameButton.setEnabled(false)
		buttonPanel.add(this.joinGameButton)
		buttonPanel.add(Spacer(50, 1))
		buttonPanel.add(Button("Quit") {
			this.sceneController.quit()
		}.setColored(Button.Colored.CAUTION))
		panel.add(buttonPanel)

		this.mainWindow = WindowComponent(Design.titleFont)
		mainWindow.isHeadless = true
		mainWindow.isMovable = false
		mainWindow.addComponent(panel)
		this.ui.addWindow(mainWindow)
	}

}
