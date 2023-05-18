package com.radovan.spring.controller

import com.radovan.spring.exceptions.{ExistingEmailException, ExistingRoomNumberException, InvalidUserException}
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.{ControllerAdvice, ExceptionHandler}

@ControllerAdvice class ExceptionController {

  @ExceptionHandler(Array(classOf[ExistingEmailException]))
  def handleExistingEmailException(): ResponseEntity[String] = ResponseEntity.internalServerError.body("Email exists already!")

  @ExceptionHandler(Array(classOf[InvalidUserException]))
  def handleInvalidUserException(): ResponseEntity[String] = ResponseEntity.internalServerError.body("Invalid user!")

  @ExceptionHandler(Array(classOf[ExistingRoomNumberException]))
  def handleExistingRoomNumberException(): ResponseEntity[String] = ResponseEntity.internalServerError.body("Existing room number!")
}

