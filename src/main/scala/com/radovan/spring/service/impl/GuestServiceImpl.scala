package com.radovan.spring.service.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.{GuestDto, UserDto}
import com.radovan.spring.entity.{GuestEntity, RoleEntity, UserEntity}
import com.radovan.spring.exceptions.ExistingEmailException
import com.radovan.spring.form.RegistrationForm
import com.radovan.spring.repository.{GuestRepository, RoleRepository, UserRepository}
import com.radovan.spring.service.GuestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util
import java.util.Optional

@Service
@Transactional
class GuestServiceImpl extends GuestService {

  @Autowired
  private val guestRepository: GuestRepository = null

  @Autowired
  private val userRepository: UserRepository = null

  @Autowired
  private val roleRepository: RoleRepository = null

  @Autowired
  private val passwordEncoder: BCryptPasswordEncoder = null

  @Autowired
  private val tempConverter: TempConverter = null

  override def addGuest(guest: GuestDto): GuestDto = {
    val guestEntity: GuestEntity = tempConverter.guestDtoToEntity(guest)
    val storedGuest: GuestEntity = guestRepository.save(guestEntity)
    val returnValue: GuestDto = tempConverter.guestEntityToDto(storedGuest)
    returnValue
  }

  override def getGuestById(guestId: Integer): GuestDto = {
    var returnValue: GuestDto = null
    val guestOpt: Optional[GuestEntity] = guestRepository.findById(guestId)
    if (guestOpt.isPresent) returnValue = tempConverter.guestEntityToDto(guestOpt.get)
    returnValue
  }

  override def deleteGuest(guestId: Integer): Unit = {
    guestRepository.deleteById(guestId)
    guestRepository.flush()
  }

  override def listAll: util.List[GuestDto] = {
    val returnValue: util.List[GuestDto] = new util.ArrayList[GuestDto]
    val allGuestsOpt: Optional[util.List[GuestEntity]] = Optional.ofNullable(guestRepository.findAll)
    if (!allGuestsOpt.isEmpty) allGuestsOpt.get.forEach((guest: GuestEntity) => {
      def foo(guest: GuestEntity) = {
        val guestDto: GuestDto = tempConverter.guestEntityToDto(guest)
        returnValue.add(guestDto)
      }

      foo(guest)
    })
    returnValue
  }

  override def storeGuest(form: RegistrationForm): GuestDto = {
    val userDto: UserDto = form.getUser
    val testUser: Optional[UserEntity] = Optional.ofNullable(userRepository.findByEmail(userDto.getEmail))
    if (testUser.isPresent) {
      val error: Error = new Error("Email exists")
      throw new ExistingEmailException(error)
    }
    val role: RoleEntity = roleRepository.findByRole("ROLE_USER")
    userDto.setPassword(passwordEncoder.encode(userDto.getPassword))
    userDto.setEnabled(1.toByte)
    val roles: util.List[RoleEntity] = new util.ArrayList[RoleEntity]
    roles.add(role)
    val userEntity: UserEntity = tempConverter.userDtoToEntity(userDto)
    userEntity.setRoles(roles)
    userEntity.setEnabled(1.toByte)
    val storedUser: UserEntity = userRepository.save(userEntity)
    val users: util.List[UserEntity] = new util.ArrayList[UserEntity]
    users.add(storedUser)
    role.setUsers(users)
    roleRepository.saveAndFlush(role)
    val guest: GuestDto = form.getGuest
    guest.setUserId(storedUser.getId)
    val guestEntity: GuestEntity = tempConverter.guestDtoToEntity(guest)
    val storedGuest: GuestEntity = guestRepository.save(guestEntity)
    val returnValue: GuestDto = tempConverter.guestEntityToDto(storedGuest)
    returnValue
  }

  override def getGuestByUserId(userId: Integer): GuestDto = {
    var returnValue: GuestDto = null
    val guestOpt: Optional[GuestEntity] = Optional.ofNullable(guestRepository.findByUserId(userId))
    if (guestOpt.isPresent) returnValue = tempConverter.guestEntityToDto(guestOpt.get)
    returnValue
  }
}

