package com.radovan.spring.service.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.RoomDto
import com.radovan.spring.entity.{RoomCategoryEntity, RoomEntity}
import com.radovan.spring.exceptions.ExistingRoomNumberException
import com.radovan.spring.repository.{RoomCategoryRepository, RoomRepository}
import com.radovan.spring.service.RoomService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util
import java.util.Optional

@Service
@Transactional
class RoomServiceImpl extends RoomService {

  @Autowired
  private val roomRepository: RoomRepository = null

  @Autowired
  private val categoryRepository: RoomCategoryRepository = null

  @Autowired
  private val tempConverter: TempConverter = null

  override def addRoom(room: RoomDto): RoomDto = {
    val categoryIdOpt: Optional[Integer] = Optional.ofNullable(room.getRoomCategoryId)
    if (categoryIdOpt.isPresent) {
      val categoryId: Integer = categoryIdOpt.get
      val categoryEntity: RoomCategoryEntity = categoryRepository.findById(categoryId).get
      room.setPrice(categoryEntity.getPrice)
    }
    val roomNumberOpt: Optional[Integer] = Optional.ofNullable(room.getRoomNumber)
    if (roomNumberOpt.isPresent) {
      val roomOpt: Optional[RoomEntity] = Optional.ofNullable(roomRepository.findByRoomNumber(roomNumberOpt.get))
      if (roomOpt.isPresent) {
        val error: Error = new Error("Existing room number")
        throw new ExistingRoomNumberException(error)
      }
    }
    val roomEntity: RoomEntity = tempConverter.roomDtoToEntity(room)
    val storedRoom: RoomEntity = roomRepository.save(roomEntity)
    val returnValue: RoomDto = tempConverter.roomEntityToDto(storedRoom)
    returnValue
  }

  override def getRoomById(roomId: Integer): RoomDto = {
    var returnValue: RoomDto = null
    val roomOpt: Optional[RoomEntity] = roomRepository.findById(roomId)
    if (roomOpt.isPresent) returnValue = tempConverter.roomEntityToDto(roomOpt.get)
    returnValue
  }

  override def deleteRoom(roomId: Integer): Unit = {
    roomRepository.deleteById(roomId)
    roomRepository.flush()
  }

  override def listAll: util.List[RoomDto] = {
    val returnValue: util.List[RoomDto] = new util.ArrayList[RoomDto]
    val allRoomsOpt: Optional[util.List[RoomEntity]] = Optional.ofNullable(roomRepository.findAll)
    if (!allRoomsOpt.isEmpty) allRoomsOpt.get.forEach((room: RoomEntity) => {
      def foo(room: RoomEntity) = {
        val roomDto: RoomDto = tempConverter.roomEntityToDto(room)
        returnValue.add(roomDto)
      }

      foo(room)
    })
    returnValue
  }

  override def listAllByCategoryId(categoryId: Integer): util.List[RoomDto] = {
    val returnValue: util.List[RoomDto] = new util.ArrayList[RoomDto]
    val allRoomsOpt: Optional[util.List[RoomEntity]] = Optional.ofNullable(roomRepository.findAllByCategoryId(categoryId))
    if (!allRoomsOpt.isEmpty) allRoomsOpt.get.forEach((room: RoomEntity) => {
      def foo(room: RoomEntity) = {
        val roomDto: RoomDto = tempConverter.roomEntityToDto(room)
        returnValue.add(roomDto)
      }

      foo(room)
    })
    returnValue
  }

  override def deleteAllByCategoryId(categoryId: Integer): Unit = {
    roomRepository.deleteAllByCategoryId(categoryId)
    roomRepository.flush()
  }
}

