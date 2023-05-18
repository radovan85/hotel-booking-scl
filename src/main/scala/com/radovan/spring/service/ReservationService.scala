package com.radovan.spring.service

import com.radovan.spring.dto.ReservationDto

import java.sql.Timestamp
import java.util

trait ReservationService {

  def addReservation(reservation: ReservationDto, roomId: Integer): ReservationDto

  def getReservationById(reservationId: Integer): ReservationDto

  def deleteReservation(reservationId: Integer): Unit

  def listAll: util.List[ReservationDto]

  def listAllByGuestId(guestId: Integer): util.List[ReservationDto]

  def listAllByRoomId(roomId: Integer): util.List[ReservationDto]

  def listAllActive: util.List[ReservationDto]

  def listAllExpired: util.List[ReservationDto]

  def isAvailable(roomId: Integer, checkInDate: Timestamp, checkOutDate: Timestamp): Boolean

  def updateReservation(reservation: ReservationDto, reservationId: Integer): ReservationDto

  def deleteAllByRoomId(roomId: Integer): Unit
}

