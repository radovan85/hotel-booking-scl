package com.radovan.spring.repository

import com.radovan.spring.entity.RoomEntity
import org.springframework.data.jpa.repository.{JpaRepository, Modifying, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

import java.util

@Repository
trait RoomRepository extends JpaRepository[RoomEntity, Integer] {

  @Query(value = "select * from rooms where category_id = :categoryId", nativeQuery = true)
  def findAllByCategoryId(@Param("categoryId") categoryId: Integer): util.List[RoomEntity]

  @Query(value = "select * from rooms where room_number = :roomNumber", nativeQuery = true)
  def findByRoomNumber(@Param("roomNumber") roomNumber: Integer): RoomEntity

  @Modifying
  @Query(value = "delete from rooms where category_id = :categoryId", nativeQuery = true)
  def deleteAllByCategoryId(@Param("categoryId") categoryId: Integer): Unit
}

