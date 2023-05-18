package com.radovan.spring.service

import com.radovan.spring.dto.RoleDto

import java.util

trait RoleService {

  def listAllAuthorities: util.List[RoleDto]

  def getRoleById(id: Integer): RoleDto
}
