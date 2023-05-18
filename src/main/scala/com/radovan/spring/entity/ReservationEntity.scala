package com.radovan.spring.entity

import jakarta.persistence.{Column, Entity, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, Table}
import org.hibernate.annotations.{CreationTimestamp, UpdateTimestamp}

import java.sql.Timestamp
import scala.beans.BeanProperty

@Entity
@Table(name = "reservations")
@SerialVersionUID(1L)
class ReservationEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "reservation_id")
  @BeanProperty var reservationId:Integer = _

  @ManyToOne
  @JoinColumn(name = "room_id")
  @BeanProperty var room:RoomEntity = _

  @ManyToOne
  @JoinColumn(name = "guest_id")
  @BeanProperty var guest:GuestEntity = _

  @Column(name = "check_in", nullable = false)
  @BeanProperty var checkInDate:Timestamp = _

  @Column(name = "check_out", nullable = false)
  @BeanProperty var checkOutDate:Timestamp = _

  @CreationTimestamp
  @Column(name = "created", nullable = false)
  @BeanProperty var createdAt:Timestamp = _

  @UpdateTimestamp
  @Column(name = "updated", nullable = false)
  @BeanProperty var updatedAt:Timestamp = _

  @Column(nullable = false)
  @BeanProperty var price:Float = _

  @Column(name = "num_of_nights", nullable = false)
  @BeanProperty var numberOfNights:Integer = _

}

