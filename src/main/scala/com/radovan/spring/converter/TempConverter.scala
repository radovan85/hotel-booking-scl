package com.radovan.spring.converter

import com.radovan.spring.dto.{GuestDto, NoteDto, ReservationDto, RoleDto, RoomCategoryDto, RoomDto, UserDto}
import com.radovan.spring.entity.{GuestEntity, NoteEntity, ReservationEntity, RoleEntity, RoomCategoryEntity, RoomEntity, UserEntity}
import com.radovan.spring.repository.{GuestRepository, RoleRepository, RoomCategoryRepository, RoomRepository, UserRepository}
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.sql.Timestamp
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util
import java.util.Optional

@Component
class TempConverter {

  @Autowired
  private val userRepository: UserRepository = null

  @Autowired
  private val roleRepository: RoleRepository = null

  @Autowired
  private val roomCategoryRepository: RoomCategoryRepository = null

  @Autowired
  private val roomRepository: RoomRepository = null

  @Autowired
  private val guestRepository: GuestRepository = null

  @Autowired
  private val mapper: ModelMapper = null

  private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

  private val decfor: DecimalFormat = new DecimalFormat("0.00")

  def guestEntityToDto(guestEntity: GuestEntity): GuestDto = {
    val returnValue: GuestDto = mapper.map(guestEntity, classOf[GuestDto])
    val userOpt: Optional[UserEntity] = Optional.ofNullable(guestEntity.getUser)
    if (userOpt.isPresent) returnValue.setUserId(userOpt.get.getId)
    returnValue
  }

  def guestDtoToEntity(guest: GuestDto): GuestEntity = {
    val returnValue: GuestEntity = mapper.map(guest, classOf[GuestEntity])
    val userIdOpt: Optional[Integer] = Optional.ofNullable(guest.getUserId)
    if (userIdOpt.isPresent) {
      val userId: Integer = userIdOpt.get
      val userEntity: UserEntity = userRepository.findById(userId).get
      returnValue.setUser(userEntity)
    }
    returnValue
  }

  def roomEntityToDto(roomEntity: RoomEntity): RoomDto = {
    val returnValue: RoomDto = mapper.map(roomEntity, classOf[RoomDto])
    val categoryOpt: Optional[RoomCategoryEntity] = Optional.ofNullable(roomEntity.getRoomCategory)
    if (categoryOpt.isPresent) returnValue.setRoomCategoryId(categoryOpt.get.getRoomCategoryId)
    returnValue
  }

  def roomDtoToEntity(room: RoomDto): RoomEntity = {
    val returnValue: RoomEntity = mapper.map(room, classOf[RoomEntity])
    val categoryIdOpt: Optional[Integer] = Optional.ofNullable(room.getRoomCategoryId)
    if (categoryIdOpt.isPresent) {
      val categoryId: Integer = categoryIdOpt.get
      val categoryEntity: RoomCategoryEntity = roomCategoryRepository.findById(categoryId).get
      returnValue.setRoomCategory(categoryEntity)
    }
    returnValue
  }

  def roomCategoryEntityToDto(categoryEntity: RoomCategoryEntity): RoomCategoryDto = {
    val returnValue: RoomCategoryDto = mapper.map(categoryEntity, classOf[RoomCategoryDto])
    val price: Float = decfor.format(returnValue.getPrice).toFloat
    returnValue.setPrice(price)
    returnValue
  }

  def roomCategoryDtoToEntity(category: RoomCategoryDto): RoomCategoryEntity = {
    val returnValue: RoomCategoryEntity = mapper.map(category, classOf[RoomCategoryEntity])
    val price: Float = decfor.format(returnValue.getPrice).toFloat
    returnValue.setPrice(price)
    returnValue
  }

  def reservationEntityToDto(reservation: ReservationEntity): ReservationDto = {
    val returnValue: ReservationDto = mapper.map(reservation, classOf[ReservationDto])
    val roomOpt: Optional[RoomEntity] = Optional.ofNullable(reservation.getRoom)
    if (roomOpt.isPresent) returnValue.setRoomId(roomOpt.get.getRoomId)
    val guestOpt: Optional[GuestEntity] = Optional.ofNullable(reservation.getGuest)
    if (guestOpt.isPresent) returnValue.setGuestId(guestOpt.get.getGuestId)
    val checkInDateOpt: Optional[Timestamp] = Optional.ofNullable(reservation.getCheckInDate)
    if (checkInDateOpt.isPresent) {
      val checkInDateStr: String = checkInDateOpt.get.toLocalDateTime.format(formatter)
      returnValue.setCheckInDateStr(checkInDateStr)
    }
    val checkOutDateOpt: Optional[Timestamp] = Optional.ofNullable(reservation.getCheckOutDate)
    if (checkOutDateOpt.isPresent) {
      val checkOutDateStr: String = checkOutDateOpt.get.toLocalDateTime.format(formatter)
      returnValue.setCheckOutDateStr(checkOutDateStr)
    }
    returnValue
  }

  def reservationDtoToEntity(reservation: ReservationDto): ReservationEntity = {
    val returnValue: ReservationEntity = mapper.map(reservation, classOf[ReservationEntity])
    val roomIdOpt: Optional[Integer] = Optional.ofNullable(reservation.getRoomId)
    if (roomIdOpt.isPresent) {
      val roomId: Integer = roomIdOpt.get
      val roomEntity: RoomEntity = roomRepository.findById(roomId).get
      returnValue.setRoom(roomEntity)
    }
    val guestIdOpt: Optional[Integer] = Optional.ofNullable(reservation.getGuestId)
    if (guestIdOpt.isPresent) {
      val guestId: Integer = guestIdOpt.get
      val guestEntity: GuestEntity = guestRepository.findById(guestId).get
      returnValue.setGuest(guestEntity)
    }
    val checkInDateStrOpt: Optional[String] = Optional.ofNullable(reservation.getCheckInDateStr)
    if (checkInDateStrOpt.isPresent) {
      val checkInDateStr: String = checkInDateStrOpt.get
      val checkInDate: LocalDateTime = LocalDateTime.parse(checkInDateStr, formatter)
      returnValue.setCheckInDate(Timestamp.valueOf(checkInDate))
    }
    val checkOutDateStrOpt: Optional[String] = Optional.ofNullable(reservation.getCheckOutDateStr)
    if (checkOutDateStrOpt.isPresent) {
      val checkOutDateStr: String = checkOutDateStrOpt.get
      val checkOutDate: LocalDateTime = LocalDateTime.parse(checkOutDateStr, formatter)
      returnValue.setCheckOutDate(Timestamp.valueOf(checkOutDate))
    }
    returnValue
  }

  def noteEntityToDto(noteEntity: NoteEntity): NoteDto = {
    val returnValue: NoteDto = mapper.map(noteEntity, classOf[NoteDto])
    val createdAtOpt: Optional[Timestamp] = Optional.ofNullable(noteEntity.getCreatedAt)
    if (createdAtOpt.isPresent) {
      val createdAtStr: String = String.valueOf(createdAtOpt.get.toLocalDateTime.format(formatter))
      returnValue.setCreatedAtStr(createdAtStr)
    }
    returnValue
  }

  def noteDtoToEntity(noteDto: NoteDto): NoteEntity = {
    val returnValue: NoteEntity = mapper.map(noteDto, classOf[NoteEntity])
    returnValue
  }

  def userEntityToDto(userEntity: UserEntity): UserDto = {
    val returnValue: UserDto = mapper.map(userEntity, classOf[UserDto])
    returnValue.setEnabled(userEntity.getEnabled)
    val rolesOpt: Optional[util.List[RoleEntity]] = Optional.ofNullable(userEntity.getRoles)
    val rolesIds: util.List[Integer] = new util.ArrayList[Integer]
    if (!rolesOpt.isEmpty) rolesOpt.get.forEach((roleEntity: RoleEntity) => {
      def foo(roleEntity: RoleEntity) = rolesIds.add(roleEntity.getId)

      foo(roleEntity)
    })
    returnValue.setRolesIds(rolesIds)
    returnValue
  }

  def userDtoToEntity(userDto: UserDto): UserEntity = {
    val returnValue: UserEntity = mapper.map(userDto, classOf[UserEntity])
    val roles: util.List[RoleEntity] = new util.ArrayList[RoleEntity]
    val rolesIdsOpt: Optional[util.List[Integer]] = Optional.ofNullable(userDto.getRolesIds)
    if (!rolesIdsOpt.isEmpty) rolesIdsOpt.get.forEach((roleId: Integer) => {
      def foo(roleId: Integer) = {
        val role: RoleEntity = roleRepository.findById(roleId).get
        roles.add(role)
      }

      foo(roleId)
    })
    returnValue.setRoles(roles)
    returnValue
  }

  def roleEntityToDto(roleEntity: RoleEntity): RoleDto = {
    val returnValue: RoleDto = mapper.map(roleEntity, classOf[RoleDto])
    val users: util.List[UserEntity] = roleEntity.getUsers
    val usersIds: util.List[Integer] = new util.ArrayList[Integer]
    users.forEach((user: UserEntity) => {
      def foo(user: UserEntity) = usersIds.add(user.getId)

      foo(user)
    })
    returnValue.setUsersIds(usersIds)
    returnValue
  }

  def roleDtoToEntity(roleDto: RoleDto): RoleEntity = {
    val returnValue: RoleEntity = mapper.map(roleDto, classOf[RoleEntity])
    val usersIds: util.List[Integer] = roleDto.getUsersIds
    val users: util.List[UserEntity] = new util.ArrayList[UserEntity]
    usersIds.forEach((userId: Integer) => {
      def foo(userId: Integer) = {
        val userEntity: UserEntity = userRepository.findById(userId).get
        users.add(userEntity)
      }

      foo(userId)
    })
    returnValue.setUsers(users)
    returnValue
  }
}

