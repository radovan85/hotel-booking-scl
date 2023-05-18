package com.radovan.spring.service

import com.radovan.spring.dto.UserDto
import com.radovan.spring.entity.UserEntity

import java.util

trait UserService {

  def updateUser(id: Integer, user: UserDto): UserDto

  def deleteUser(id: Integer): Unit

  def getUserById(id: Integer): UserDto

  def listAllUsers: util.List[UserDto]

  def getUserByEmail(email: String): UserEntity

  def storeUser(user: UserDto): UserDto

  def getCurrentUser: UserDto

  def suspendUser(userId: Integer): Unit
}
