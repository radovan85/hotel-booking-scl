package com.radovan.spring.dto

import java.sql.Timestamp
import scala.beans.BeanProperty

@SerialVersionUID(1L)
class NoteDto extends Serializable {

  @BeanProperty var noteId:Integer = _
  @BeanProperty var subject:String = _
  @BeanProperty var text:String = _
  @BeanProperty var createdAt:Timestamp = _
  @BeanProperty var createdAtStr:String = _

}

