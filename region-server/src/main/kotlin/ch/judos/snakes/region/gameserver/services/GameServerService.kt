package ch.judos.snakes.region.gameserver.services

import ch.judos.snakes.common.dto.GameserverConnectDto
import ch.judos.snakes.common.messages.game.RegionLogin
import ch.judos.snakes.common.messages.region.GameUpdate
import ch.judos.snakes.common.messages.region.LobbyInfo
import ch.judos.snakes.common.model.Connection
import ch.judos.snakes.region.core.entity.AdminUser
import ch.judos.snakes.region.extension.firstMissingNumber
import ch.judos.snakes.region.gameserver.model.GameServer
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.net.Socket

@Service
class GameServerService {
	private val logger = LoggerFactory.getLogger(javaClass)

	val servers: MutableMap<Long, GameServer> = HashMap()

	fun register(user: AdminUser, request: GameserverConnectDto): Int {
		var server: GameServer
		synchronized(servers) {
			servers.remove(user.id)
			logger.info("register server ${user.id}")
			val serverNr = getServerNumber()
			server =
				GameServer(request.host, request.port, request.gameModes, serverNr)
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
			try {
				connection.out.writeUnshared(RegionLogin(request.token))
				connection.out.flush()
				while (true) {
					val data = connection.inp.readObject()
					if (data is GameUpdate) {
						logger.info("update from $server: $data")
						server.update(data.currentLoad, data.lobbies)
					} else {
						logger.info("unknown msg from $server: $data")
					}
				}
			} finally {
				connection.close()
				synchronized(this.servers) {
					this.servers.remove(user.id)
				}
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

	@Scheduled(fixedRate = 30 * 1000)
	private fun cleanup() {
		synchronized(servers) {
			val it = servers.iterator()
			while (it.hasNext()) {
				val server = it.next().value
				if (server.isOlderThanS(62)) {
					logger.info("Timeout game-server $server")
					it.remove()
				}
			}
		}
	}

	fun gameModes(): List<String> {
		return this.servers.values.flatMap { it.gameModes.asIterable() }
	}

	fun getLobbies(): Collection<LobbyInfo> {
		return this.servers.flatMap { it.value.lobbies }
	}

}
