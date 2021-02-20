package ch.judos.snakes.region.core.config.auth

import ch.judos.snakes.region.core.config.UserDetailsAdminService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig @Autowired constructor(
		private val userDetailsAdminService: UserDetailsAdminService,
		private val jwtRequestFilter: JwtRequestFilter
) : WebSecurityConfigurerAdapter() {

	@Autowired
	@Throws(Exception::class)
	fun configureGlobal(auth: AuthenticationManagerBuilder, passwordEncoder: PasswordEncoder) {
		// Username password authentication for admin ui panel
		auth.authenticationProvider(DaoAuthenticationProvider().also {
			it.setUserDetailsService(userDetailsAdminService)
			it.setPasswordEncoder(passwordEncoder)
		})
	}

	@Bean
	fun passwordEncoder(): PasswordEncoder {
		return BCryptPasswordEncoder()
	}

	@Bean
	@Throws(Exception::class)
	override fun authenticationManagerBean(): AuthenticationManager {
		return super.authenticationManagerBean()
	}

	@Throws(Exception::class)
	override fun configure(httpSecurity: HttpSecurity) {
		httpSecurity.csrf().disable()
		httpSecurity.cors()

		// Admin UI generally locked, except login method
		httpSecurity.authorizeRequests().antMatchers("/authenticate").permitAll()

		// App endpoints, some open, some require login
		httpSecurity.authorizeRequests().antMatchers("/public/**").permitAll()
		httpSecurity.authorizeRequests().antMatchers("/test/**").permitAll()
		httpSecurity.authorizeRequests().antMatchers("/admin/**").hasAuthority("ADMIN")
		httpSecurity.authorizeRequests().antMatchers("/user/**").hasAuthority("APP")

		httpSecurity.authorizeRequests().antMatchers("/swagger-ui.html").permitAll()
		httpSecurity.authorizeRequests().antMatchers("/swagger-ui/**").permitAll()
		httpSecurity.authorizeRequests().antMatchers("/v3/api-docs/**").permitAll()
		httpSecurity.authorizeRequests().anyRequest().permitAll()

		httpSecurity.exceptionHandling().authenticationEntryPoint(JwtAuthenticationEntryPoint())
				.accessDeniedHandler(JwtAccessDeniedHandler())
		// make sure we use stateless session; session won't be used to store user's state.
		httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
	}

}
