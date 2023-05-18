package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class GuestDto extends Serializable {

  @BeanProperty var guestId:Integer = _
  @BeanProperty var phoneNumber:String = _
  @BeanProperty var idNumber: java.lang.Long = _
  @BeanProperty var userId:Integer = _

}

