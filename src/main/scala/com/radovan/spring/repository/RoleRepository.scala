package com.radovan.spring.repository

import com.radovan.spring.entity.RoleEntity
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

import java.util

@Repository
trait RoleRepository extends JpaRepository[RoleEntity, Integer] {

  def findByRole(roleName: String): RoleEntity

  @Query(value = "select user_id from users_roles where roles_id = :roleId", nativeQuery = true)
  def findUsersIds(@Param("roleId") roleId: Integer): util.List[Integer]
}
