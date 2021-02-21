package ch.judos.snakes.region.core.model

@FunctionalInterface
fun interface RunnableWithProgressAndException {
	@Throws(Exception::class, BusinessException::class)
	fun run(p: AsyncJob)
}
