package com.radovan.spring.security

import com.radovan.spring.security.handler.LoginSuccessHandler
import com.radovan.spring.service.impl.UserDetailsImpl
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.security.authentication.{AuthenticationManager, ProviderManager}
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.{HttpSecurity, WebSecurity}
import org.springframework.security.config.annotation.web.configuration.{EnableWebSecurity, WebSecurityCustomizer}
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfiguration {

  @Bean
  def authenticationManager: AuthenticationManager = {
    val authProvider = new DaoAuthenticationProvider
    authProvider.setUserDetailsService(userDetailsService)
    authProvider.setPasswordEncoder(passwordEncoder)
    new ProviderManager(authProvider)
  }

  @Bean
  @throws[Exception]
  protected def configure(http: HttpSecurity): SecurityFilterChain = {

    http.formLogin
      .loginPage("/login").successHandler(new LoginSuccessHandler).loginProcessingUrl("/login")
      .usernameParameter("email").passwordParameter("password").permitAll

    http.logout.permitAll.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
      .logoutSuccessUrl("/login?logout").and.csrf.disable

    http.authorizeHttpRequests
      .requestMatchers("/login").anonymous
      .requestMatchers("/loginErrorPage", "/", "/contactInfo").permitAll
      .requestMatchers("/home", "/registerComplete", "/registerFail", "/loginPassConfirm").permitAll
      .requestMatchers("/admin").hasAuthority("ADMIN")
      .requestMatchers("/guests").hasAuthority("ROLE_USER")
      .requestMatchers("/register").anonymous
      .anyRequest.authenticated
    http.build
  }

  @Bean
  def userDetailsService = new UserDetailsImpl

  @Bean
  def passwordEncoder = new BCryptPasswordEncoder

  @Bean
  def resourcesCustomizer: WebSecurityCustomizer = (web: WebSecurity) =>
    web.ignoring.requestMatchers("/resources/**", "/static/**", "/images/**", "/css/**", "/js/**")
}

