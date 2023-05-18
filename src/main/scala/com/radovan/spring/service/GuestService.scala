package com.radovan.spring.service

import com.radovan.spring.dto.GuestDto
import com.radovan.spring.form.RegistrationForm

import java.util

trait GuestService {

  def addGuest(guest: GuestDto): GuestDto

  def getGuestById(guestId: Integer): GuestDto

  def getGuestByUserId(userId: Integer): GuestDto

  def deleteGuest(guestId: Integer): Unit

  def listAll: util.List[GuestDto]

  def storeGuest(form: RegistrationForm): GuestDto
}
