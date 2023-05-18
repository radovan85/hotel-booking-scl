package com.radovan.spring.form

import com.radovan.spring.dto.{GuestDto, UserDto}

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class RegistrationForm extends Serializable {

  @BeanProperty var user:UserDto = _
  @BeanProperty var guest:GuestDto = _

}

