package com.radovan.spring.service

import com.radovan.spring.dto.RoomDto

import java.util

trait RoomService {

  def addRoom(room: RoomDto): RoomDto

  def getRoomById(roomId: Integer): RoomDto

  def deleteRoom(roomId: Integer): Unit

  def listAll: util.List[RoomDto]

  def listAllByCategoryId(categoryId: Integer): util.List[RoomDto]

  def deleteAllByCategoryId(categoryId: Integer): Unit
}

