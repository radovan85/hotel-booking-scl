package com.radovan.spring.entity

import jakarta.persistence.{Column, Entity, GeneratedValue, GenerationType, Id, Table}

import scala.beans.BeanProperty

@Entity
@Table(name = "room_categories")
@SerialVersionUID(1L)
class RoomCategoryEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id")
  @BeanProperty var roomCategoryId:Integer = _

  @Column(nullable = false, length = 30)
  @BeanProperty var name:String = _

  @Column(nullable = false)
  @BeanProperty var price:Float = _

  @Column(nullable = false)
  @BeanProperty var wifi:Byte = _

  @Column(nullable = false)
  @BeanProperty var wc:Byte = _

  @Column(nullable = false)
  @BeanProperty var tv:Byte = _

  @Column(nullable = false)
  @BeanProperty var bar:Byte = _

}

