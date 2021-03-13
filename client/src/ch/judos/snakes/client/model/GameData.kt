package ch.judos.snakes.client.model

class GameData {

	val loadingData = LoadingData()

	val playerData = PlayerData()
	val lobbyData = LobbyData()

	val settings = ClientSettings()
	val config = ClientConfig.load()


}