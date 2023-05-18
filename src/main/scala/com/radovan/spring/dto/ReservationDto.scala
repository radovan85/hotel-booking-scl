package com.radovan.spring.dto

import java.sql.Timestamp
import java.time.LocalDateTime
import scala.beans.BeanProperty

@SerialVersionUID(1L)
class ReservationDto extends Serializable {

  @BeanProperty var reservationId:Integer = _
  @BeanProperty var roomId:Integer = _
  @BeanProperty var guestId:Integer = _
  @BeanProperty var checkInDate:Timestamp = _
  @BeanProperty var checkOutDate:Timestamp = _
  @BeanProperty var checkInDateStr:String = _
  @BeanProperty var checkOutDateStr:String = _
  @BeanProperty var createdAt:Timestamp = _
  @BeanProperty var updatedAt:Timestamp = _
  @BeanProperty var price:Float = _
  @BeanProperty var numberOfNights:Integer = _

  def possibleCancel: Boolean = {
    var returnValue = false
    val currentDate = LocalDateTime.now
    val cancelDate = currentDate.plusDays(1)
    val checkInDateTime = checkInDate.toLocalDateTime
    if (cancelDate.isBefore(checkInDateTime)) returnValue = true
    returnValue
  }

}
