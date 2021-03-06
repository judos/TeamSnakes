package ch.judos.snakes.common.service

import java.security.SecureRandom

class RandomService {

	private val random: SecureRandom = SecureRandom()

	fun generateToken(length: Int): String {
		var result = ""
		for (char in 1..length) result += ALPHA_NUM[random.nextInt(ALPHA_NUM.length)]
		return result
	}

	fun generateTokens(amount: Int, length: Int): MutableList<String> {
		val tokens = mutableListOf<String>()
		for (i in 1..amount) {
			tokens.add(generateToken(length))
		}
		return tokens
	}

	companion object {
		@Suppress("SpellCheckingInspection")
		private const val LETTERS_UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

		@Suppress("SpellCheckingInspection")
		private const val LETTERS_LOWERCASE = "abcdefghijklmnopqrstuvwxyz"


		private const val DIGITS = "0123456789"

		private const val ALPHA_NUM = LETTERS_UPPERCASE + DIGITS + LETTERS_LOWERCASE
	}


}
