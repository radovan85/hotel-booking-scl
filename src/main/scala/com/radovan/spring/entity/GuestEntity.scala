package com.radovan.spring.entity

import jakarta.persistence.{CascadeType, Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, OneToOne, Table}

import scala.beans.BeanProperty

@Entity
@Table(name = "guests")
@SerialVersionUID(1L)
class GuestEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "guest_id")
  @BeanProperty var guestId:Integer = _

  @Column(name = "phone_number", nullable = false, length = 15)
  @BeanProperty var phoneNumber:String = _

  @Column(name = "id_number", nullable = false, length = 12)
  @BeanProperty var idNumber: java.lang.Long = _

  @OneToOne(cascade = Array(CascadeType.MERGE), fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  @BeanProperty var user:UserEntity = _


}

