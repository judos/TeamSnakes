package ch.judos.snakes.region.gameserver.services

import ch.judos.snakes.common.dto.GameserverConnectDto
import ch.judos.snakes.common.model.Connection
import ch.judos.snakes.region.core.entity.AdminUser
import ch.judos.snakes.region.extension.firstMissingNumber
import ch.judos.snakes.region.gameserver.dto.LobbyDto
import ch.judos.snakes.region.gameserver.dto.RegisterDto
import ch.judos.snakes.region.gameserver.model.GameServer
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.net.Socket

@Service
class GameServerService {
	private val logger = LoggerFactory.getLogger(javaClass)

	val servers: MutableMap<Long, GameServer> = HashMap()

	val lobbies: HashMap<String, LobbyDto> = HashMap()

	fun register(user: AdminUser, request: GameserverConnectDto): Int {
		var server = servers[user.id]
		if (server != null) {
			logger.info("update server ${user.id}")
		} else {
			synchronized(servers) {
				val serverNr = getServerNumber()
				server =
					GameServer(request.host, request.port, request.gameModes, serverNr)
				servers[user.id] = server!!
			}
		}
		listenToGameServer(server!!, request)
		return server!!.serverNr
	}

	private fun listenToGameServer(server: GameServer, request: GameserverConnectDto) {
		val thread = Thread({
			val socket = Socket(request.host, request.port)
			val connection = Connection(socket) {}
			connection.out.println("region:${request.token}")
			Thread.sleep(30000)
			connection.out.flush()
			Thread.sleep(30000)
			connection.close()
		}, "Game Server $server")
		thread.isDaemon = true
		thread.start()
	}


	fun update(user: AdminUser, request: RegisterDto) {
//		var server = servers[user.id]
//		if (server != null) {
//			server.lobbies.forEach { lobbies.remove(it.name) }
//			request.lobbies.forEach { lobbies[it.name] = it }
//			server.update(request.url, request.gameModes, request.currentLoad, request.lobbies)
//		} else {
//			synchronized(servers) {
//				val serverNr = getServerNumber()
//				server =
//					GameServer(request.url, request.gameModes, request.currentLoad, serverNr, request.lobbies)
//				request.lobbies.forEach { lobbies[it.name] = it }
//				servers[user.id] = server!!
//			}
//		}
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
					server.lobbies.forEach { lobbies.remove(it.name) }
				}
			}
		}
	}

	fun gameModes(): List<String> {
		return this.servers.values.flatMap { it.gameModes.asIterable() }
	}

}
