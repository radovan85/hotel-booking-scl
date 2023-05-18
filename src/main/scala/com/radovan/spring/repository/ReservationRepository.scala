package com.radovan.spring.repository

import com.radovan.spring.entity.ReservationEntity
import org.springframework.data.jpa.repository.{JpaRepository, Modifying, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

import java.sql.Timestamp
import java.util

@Repository
trait ReservationRepository extends JpaRepository[ReservationEntity, Integer] {

  @Query(value = "select * from reservations where room_id = :roomId", nativeQuery = true)
  def findAllByRoomId(@Param("roomId") roomId: Integer): util.List[ReservationEntity]

  @Query(value = "select * from reservations where guest_id = :guestId", nativeQuery = true)
  def findAllByGuestId(@Param("guestId") guestId: Integer): util.List[ReservationEntity]

  @Query(value = "select * from reservations where room_id = :roomId and (check_in between :checkInDate and :checkOutDate or check_out between :checkInDate and :checkOutDate)", nativeQuery = true)
  def checkForReservations(@Param("roomId") roomId: Integer, @Param("checkInDate") checkInDate: Timestamp, @Param("checkOutDate") checkOutDate: Timestamp): util.List[ReservationEntity]

  @Modifying
  @Query(value = "delete from reservations where room_id = :roomId", nativeQuery = true)
  def deleteAllByRoomId(@Param("roomId") roomId: Integer): Unit
}

