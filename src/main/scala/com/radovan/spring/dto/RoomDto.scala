package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class RoomDto extends Serializable {

  @BeanProperty var roomId:Integer = _
  @BeanProperty var roomNumber:Integer = _
  @BeanProperty var price:Float = _
  @BeanProperty var roomCategoryId:Integer = _

  override def toString: String = "RoomDto [roomId=" + roomId + ", roomNumber=" + roomNumber + ", price=" + price + ", roomCategoryId=" + roomCategoryId + "]"
}

