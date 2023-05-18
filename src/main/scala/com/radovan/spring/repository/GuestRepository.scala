package com.radovan.spring.repository

import com.radovan.spring.entity.GuestEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait GuestRepository extends JpaRepository[GuestEntity, Integer] {

  def findByUserId(userId: Integer): GuestEntity
}
