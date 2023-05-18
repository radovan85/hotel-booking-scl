package com.radovan.spring.entity

import jakarta.persistence.{Column, Entity, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, Table}

import scala.beans.BeanProperty

@Entity
@Table(name = "rooms")
@SerialVersionUID(1L)
class RoomEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "room_id")
  @BeanProperty var roomId:Integer = _

  @Column(name = "room_number", nullable = false)
  @BeanProperty var roomNumber:Integer = _

  @Column(nullable = false)
  @BeanProperty var price:Float = _

  @ManyToOne
  @JoinColumn(name = "category_id")
  @BeanProperty var roomCategory:RoomCategoryEntity = _

}

