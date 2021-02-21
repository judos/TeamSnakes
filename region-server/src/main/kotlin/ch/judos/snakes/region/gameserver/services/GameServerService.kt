package ch.judos.snakes.region.gameserver.services

import ch.judos.snakes.region.core.entity.AdminUser
import ch.judos.snakes.region.extension.firstMissingNumber
import ch.judos.snakes.region.gameserver.dto.LobbyDto
import ch.judos.snakes.region.gameserver.dto.RegisterDto
import ch.judos.snakes.region.gameserver.model.GameServer
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class GameServerService {

	val servers: MutableMap<Long, GameServer> = HashMap()

	val lobbies: HashMap<String, LobbyDto> = HashMap()

	fun register(user: AdminUser, request: RegisterDto): Int {
		var server = servers[user.id]
		if (server != null) {
			server.lobbies.forEach { lobbies.remove(it.name) }
			request.lobbies.forEach { lobbies[it.name] = it }
			server.update(request.url, request.gameModes, request.currentLoad, request.lobbies)
		} else {
			synchronized(servers) {
				val serverNr = getServerNumber()
				server =
					GameServer(request.url, request.gameModes, request.currentLoad, serverNr, request.lobbies)
				servers[user.id] = server!!
			}
		}
		return server!!.serverNr
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
