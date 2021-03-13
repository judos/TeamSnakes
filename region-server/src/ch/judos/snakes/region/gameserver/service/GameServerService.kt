package ch.judos.snakes.region.gameserver.service

import ch.judos.snakes.common.dto.GameserverConnectDto
import ch.judos.snakes.common.messages.game.RegionLogin
import ch.judos.snakes.common.messages.region.LobbyUpdateList
import ch.judos.snakes.common.model.Connection
import ch.judos.snakes.common.model.Lobby
import ch.judos.snakes.region.client.service.ClientService
import ch.judos.snakes.region.core.entity.AdminUser
import ch.judos.snakes.region.extension.firstMissingNumber
import ch.judos.snakes.region.gameserver.model.GameServer
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.net.Socket
import java.net.SocketException

@Service
class GameServerService {
	private val logger = LoggerFactory.getLogger(javaClass)

	var clientService: ClientService? = null
	val servers: MutableMap<Long, GameServer> = HashMap()

	fun register(user: AdminUser, request: GameserverConnectDto): Int {
		var server: GameServer
		synchronized(servers) {
			servers.remove(user.id)
			val serverNr = getServerNumber()
			logger.info("register server ${user.id} with server number $serverNr")
			server =
					GameServer(user.username, request.host, request.port, request.gameModes, serverNr)
			servers[user.id] = server
		}
		listenToGameServer(user, server, request)
		return server.serverNr
	}

	private fun listenToGameServer(user: AdminUser, server: GameServer,
			request: GameserverConnectDto) {
		val thread = Thread({
			val socket = Socket(request.host, request.port)
			val connection = Connection(socket) {}
			server.connection = connection
			try {
				connection.writeObject(RegionLogin(request.token))
				while (true) {
					val data = connection.inp.readObject()
					if (data is LobbyUpdateList) {
						val updated = server.update(data.currentLoad, data.lobbies)
						if (updated) {
							this.clientService?.sendLobbyUpdates()
						}
					} else {
						logger.info("unknown msg from $server: $data")
					}
				}
			} catch (e: SocketException) {
				logger.info("Game Server $server socket closed")
			} finally {
				connection.close()
				synchronized(this.servers) {
					this.servers.remove(user.id)
				}
				this.clientService?.sendLobbyUpdates()
			}
		}, "Game Server $server")
		thread.isDaemon = true
		thread.start()
	}

	fun getServerNumber(): Int {
		synchronized(servers) {
			val nrsUsed = servers.map { it.value.serverNr }.toHashSet()
			return nrsUsed.firstMissingNumber()
		}
	}

//	@Scheduled(fixedRate = 30 * 1000)
//	private fun cleanup() {
//	}

	fun gameModes(): List<String> {
		return this.servers.values.flatMap { it.gameModes.asIterable() }
	}

	fun getLobbies(): List<Lobby> {
		return this.servers.flatMap { it.value.lobbies }
	}

	fun chooseServerForLobby(mode: String): GameServer? {
		if (this.servers.size == 0)
			return null
		// TODO: implement load balancing & check mode
		return this.servers.entries.first().value
	}

}
