package com.radovan.spring.controller

import com.radovan.spring.exceptions.InvalidUserException
import com.radovan.spring.form.RegistrationForm
import com.radovan.spring.service.GuestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{GetMapping, ModelAttribute, PostMapping}

import java.security.Principal
import java.util.Optional

//noinspection ScalaUnusedSymbol
@Controller
class MainController {

  @Autowired
  private val guestService:GuestService = null

  @GetMapping(value = Array("/"))
  def indexPage = "index"

  @GetMapping(value = Array("/home"))
  def homePage = "fragments/homePage :: ajaxLoadedContent"

  @GetMapping(value = Array("/login"))
  def login = "fragments/login :: ajaxLoadedContent"

  @PostMapping(value = Array("/loginPassConfirm"))
  def confirmLoginPass(principal: Principal): String = {
    val authPrincipal = Optional.ofNullable(principal)
    if (!authPrincipal.isPresent) {
      val error = new Error("Invalid user")
      throw new InvalidUserException(error)
    }
    "fragments/homePage :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/loginErrorPage"))
  def logError(map: ModelMap): String = {
    map.put("alert", "Invalid username or password!")
    "fragments/login :: ajaxLoadedContent"
  }

  @PostMapping(value = Array("/loggedout"))
  def logout: String = {
    val context = SecurityContextHolder.getContext
    context.setAuthentication(null)
    SecurityContextHolder.clearContext()
    "fragments/homePage :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/contactInfo"))
  def getContactInfo = "fragments/contact :: ajaxLoadedContent"

  @GetMapping(value = Array("/register"))
  def userRegistration(map: ModelMap): String = {
    val registerForm = new RegistrationForm
    map.put("registerForm", registerForm)
    "fragments/registration :: ajaxLoadedContent"
  }

  @PostMapping(value = Array("/register"))
  def registerUser(@ModelAttribute("registerForm") registerForm: RegistrationForm): String = {
    guestService.storeGuest(registerForm)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/registerComplete"))
  def registrationCompleted = "fragments/registration_completed :: ajaxLoadedContent"

  @GetMapping(value = Array("/registerFail"))
  def registrationFailed = "fragments/registration_failed :: ajaxLoadedContent"
}

