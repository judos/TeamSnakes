package ch.judos.snakes.region.core.config.auth

import io.jsonwebtoken.JwtException


class OutdatedJwtException(message: String) : JwtException(message)
