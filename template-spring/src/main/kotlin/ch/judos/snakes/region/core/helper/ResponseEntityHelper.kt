package ch.judos.snakes.region.core.helper

import ch.judos.snakes.region.core.dto.ErrorDto
import org.springframework.http.ResponseEntity


class ResponseEntityHelper {
	companion object {

		fun tooManyAttempts(): ResponseEntity<Any> {
			return ResponseEntity.badRequest()
					.body(ErrorDto("TOO_MANY_ATTEMPTS", "You are blocked due to too many recent attempts"))
		}

		fun invalidOnboardingCode(): ResponseEntity<Any> {
			return ResponseEntity.badRequest().body(ErrorDto("INVALID_CODE", "This onboarding code is unknown"))
		}

		fun onboardingCodeAlreadyUsed(): ResponseEntity<Any> {
			return ResponseEntity.badRequest().body(ErrorDto("CODE_ALREADY_USED", "This onboarding code is already used."))
		}

		fun missingInformationEmailPhone(): ResponseEntity<Any> {
			return ResponseEntity.badRequest()
					.body(ErrorDto("MISSING_INFORMATION", "You need to provide at least one of email and phone"))
		}

		fun invalidCredentials(): ResponseEntity<Any> {
			return ResponseEntity.badRequest()
					.body(ErrorDto("INVALID_CREDENTIALS", "Provided credentials are invalid"))
		}

		fun otpExpired(): ResponseEntity<Any> {
			return ResponseEntity.badRequest()
					.body(ErrorDto("OTP_EXPIRED", "The given otp has expired"))
		}

		fun disabledUser(): ResponseEntity<Any> {
			return ResponseEntity.badRequest().body(ErrorDto("DISABLED_USER", "User is disabled"))
		}

	}
}
