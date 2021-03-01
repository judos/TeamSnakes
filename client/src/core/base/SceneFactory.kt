package core.base

import org.apache.logging.log4j.LogManager
import java.util.*
import java.util.function.Supplier

class SceneFactory(
) {

	protected val logger = LogManager.getLogger(javaClass)!!
	private val generators: HashMap<Class<out Scene>, Supplier<Scene>> = HashMap()

	fun createScene(sceneClass: Class<out Scene>): Scene? {
		return generators[sceneClass]?.get()
	}

	fun <T : Scene> register(sceneClass: Class<T>, generator: Supplier<Scene>) {
		generators[sceneClass] = generator
	}

	private fun mapArguments(args: Array<Any>): HashMap<Class<*>, Any> {
		val result = HashMap<Class<*>, Any>()
		for (arg in args) result[arg.javaClass] = arg
		return result
	}

}
