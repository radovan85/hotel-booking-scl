package com.radovan.spring.service.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.ReservationDto
import com.radovan.spring.entity.{NoteEntity, ReservationEntity, RoleEntity, RoomEntity, UserEntity}
import com.radovan.spring.repository.{NoteRepository, ReservationRepository, RoomRepository, UserRepository}
import com.radovan.spring.service.ReservationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.sql.Timestamp
import java.time.LocalDateTime
import java.util
import java.util.Optional

@Service
@Transactional class ReservationServiceImpl extends ReservationService {

  @Autowired
  private val reservationRepository: ReservationRepository = null

  @Autowired
  private val tempConverter: TempConverter = null

  @Autowired
  private val roomRepository: RoomRepository = null

  @Autowired
  private val noteRepository: NoteRepository = null

  @Autowired
  private val userRepository: UserRepository = null

  override def addReservation(reservation: ReservationDto, roomId: Integer): ReservationDto = {
    reservation.setRoomId(roomId)
    val roomOpt: Optional[RoomEntity] = roomRepository.findById(roomId)
    if (roomOpt.isPresent) {
      val reservationPrice: Float = reservation.getNumberOfNights * roomOpt.get.getPrice
      reservation.setPrice(reservationPrice)
    }
    val reservationEntity: ReservationEntity = tempConverter.reservationDtoToEntity(reservation)
    val storedReservation: ReservationEntity = reservationRepository.save(reservationEntity)
    val returnValue: ReservationDto = tempConverter.reservationEntityToDto(storedReservation)
    val noteEntity: NoteEntity = new NoteEntity
    var text: String = ""
    noteEntity.setSubject("Reservation Created")
    val authUser: UserEntity = SecurityContextHolder.getContext.getAuthentication.getPrincipal.asInstanceOf[UserEntity]
    val userEntity: UserEntity = userRepository.findById(authUser.getId).get
    text = "User " + userEntity.getFirstName + " " + userEntity.getLastName + " reserved the room " + storedReservation.getRoom.getRoomNumber + ".Check-in is scheduled for " + returnValue.getCheckInDateStr
    noteEntity.setText(text)
    noteRepository.save(noteEntity)
    returnValue
  }

  override def getReservationById(reservationId: Integer): ReservationDto = {
    var returnValue: ReservationDto = null
    val reservationOpt: Optional[ReservationEntity] = reservationRepository.findById(reservationId)
    if (reservationOpt.isPresent) returnValue = tempConverter.reservationEntityToDto(reservationOpt.get)
    returnValue
  }

  override def deleteReservation(reservationId: Integer): Unit = {
    val reservation: ReservationEntity = reservationRepository.findById(reservationId).get
    val reservationDto: ReservationDto = tempConverter.reservationEntityToDto(reservation)
    val roomEntity: RoomEntity = roomRepository.findById(reservation.getRoom.getRoomId).get
    val noteEntity: NoteEntity = new NoteEntity
    noteEntity.setSubject("Reservation Canceled")
    val authUser: UserEntity = SecurityContextHolder.getContext.getAuthentication.getPrincipal.asInstanceOf[UserEntity]
    val userEntity: UserEntity = userRepository.findById(authUser.getId).get
    val userRoles: util.List[RoleEntity] = userEntity.getRoles
    userRoles.forEach((role: RoleEntity) => {
      def foo(role: RoleEntity) = if (role.getRole.contains("ROLE_USER")) {
        var text: String = ""
        text = "Reservation for user " + userEntity.getFirstName + " " + userEntity.getLastName + " for room No " + roomEntity.getRoomNumber + " scheduled for " + reservationDto.getCheckInDateStr + " has been cancelled by user"
        noteEntity.setText(text)
        noteRepository.saveAndFlush(noteEntity)
      }

      foo(role)
    })
    reservationRepository.deleteById(reservationId)
    reservationRepository.flush()
  }

  override def listAll: util.List[ReservationDto] = {
    val returnValue: util.List[ReservationDto] = new util.ArrayList[ReservationDto]
    val allReservationsOpt: Optional[util.List[ReservationEntity]] = Optional.ofNullable(reservationRepository.findAll)
    if (!allReservationsOpt.isEmpty) allReservationsOpt.get.forEach((reservation: ReservationEntity) => {
      def foo(reservation: ReservationEntity) = {
        val reservationDto: ReservationDto = tempConverter.reservationEntityToDto(reservation)
        returnValue.add(reservationDto)
      }

      foo(reservation)
    })
    returnValue
  }

  override def listAllByGuestId(guestId: Integer): util.List[ReservationDto] = {
    val returnValue: util.List[ReservationDto] = new util.ArrayList[ReservationDto]
    val allReservationsOpt: Optional[util.List[ReservationEntity]] = Optional.ofNullable(reservationRepository.findAllByGuestId(guestId))
    if (!allReservationsOpt.isEmpty) allReservationsOpt.get.forEach((reservation: ReservationEntity) => {
      def foo(reservation: ReservationEntity) = {
        val reservationDto: ReservationDto = tempConverter.reservationEntityToDto(reservation)
        returnValue.add(reservationDto)
      }

      foo(reservation)
    })
    returnValue
  }

  override def listAllByRoomId(roomId: Integer): util.List[ReservationDto] = {
    val returnValue: util.List[ReservationDto] = new util.ArrayList[ReservationDto]
    val allReservationsOpt: Optional[util.List[ReservationEntity]] = Optional.ofNullable(reservationRepository.findAllByRoomId(roomId))
    if (!allReservationsOpt.isEmpty) allReservationsOpt.get.forEach((reservation: ReservationEntity) => {
      def foo(reservation: ReservationEntity) = {
        val reservationDto: ReservationDto = tempConverter.reservationEntityToDto(reservation)
        returnValue.add(reservationDto)
      }

      foo(reservation)
    })
    returnValue
  }

  override def isAvailable(roomId: Integer, checkInDate: Timestamp, checkOutDate: Timestamp): Boolean = {
    var returnValue: Boolean = false
    val allReservationsOpt: util.List[ReservationEntity] = reservationRepository.checkForReservations(roomId, checkInDate, checkOutDate)
    if (!allReservationsOpt.isEmpty) returnValue = false
    else returnValue = true
    returnValue
  }

  override def updateReservation(reservation: ReservationDto, reservationId: Integer): ReservationDto = {
    val currentReservation: ReservationEntity = reservationRepository.findById(reservationId).get
    val reservationEntity: ReservationEntity = tempConverter.reservationDtoToEntity(reservation)
    reservationEntity.setReservationId(reservationId)
    reservationEntity.setCreatedAt(currentReservation.getCreatedAt)
    val storedReservation: ReservationEntity = reservationRepository.saveAndFlush(reservationEntity)
    val returnValue: ReservationDto = tempConverter.reservationEntityToDto(storedReservation)
    val noteEntity: NoteEntity = new NoteEntity
    noteEntity.setSubject("Reservation Updated")
    val text: String = "Reservation " + returnValue.getReservationId + " scheduled for " + returnValue.getCheckInDateStr + " has been switched to room " + storedReservation.getRoom.getRoomNumber
    noteEntity.setText(text)
    noteRepository.saveAndFlush(noteEntity)
    returnValue
  }

  override def listAllActive: util.List[ReservationDto] = {
    val returnValue: util.List[ReservationDto] = new util.ArrayList[ReservationDto]
    val allReservationsOpt: Optional[util.List[ReservationEntity]] = Optional.ofNullable(reservationRepository.findAll)
    val currentDate: LocalDateTime = LocalDateTime.now
    if (!allReservationsOpt.isEmpty) allReservationsOpt.get.forEach((reservation: ReservationEntity) => {
      def foo(reservation: ReservationEntity) = if (reservation.getCheckOutDate.toLocalDateTime.isAfter(currentDate)) {
        val reservationDto: ReservationDto = tempConverter.reservationEntityToDto(reservation)
        returnValue.add(reservationDto)
      }

      foo(reservation)
    })
    returnValue
  }

  override def listAllExpired: util.List[ReservationDto] = {
    val returnValue: util.List[ReservationDto] = new util.ArrayList[ReservationDto]
    val allReservationsOpt: Optional[util.List[ReservationEntity]] = Optional.ofNullable(reservationRepository.findAll)
    val currentDate: LocalDateTime = LocalDateTime.now
    if (!allReservationsOpt.isEmpty) allReservationsOpt.get.forEach((reservation: ReservationEntity) => {
      def foo(reservation: ReservationEntity) = if (reservation.getCheckOutDate.toLocalDateTime.isBefore(currentDate)) {
        val reservationDto: ReservationDto = tempConverter.reservationEntityToDto(reservation)
        returnValue.add(reservationDto)
      }

      foo(reservation)
    })
    returnValue
  }

  override def deleteAllByRoomId(roomId: Integer): Unit = {
    reservationRepository.deleteAllByRoomId(roomId)
    reservationRepository.flush()
  }
}

