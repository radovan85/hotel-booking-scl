package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class RoomCategoryDto extends Serializable {

  @BeanProperty var roomCategoryId:Integer = _
  @BeanProperty var name:String = _
  @BeanProperty var price:Float = _
  @BeanProperty var wifi:Byte = _
  @BeanProperty var wc:Byte = _
  @BeanProperty var tv:Byte = _
  @BeanProperty var bar:Byte = _

}

