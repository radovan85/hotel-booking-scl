package com.radovan.spring.service.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.RoleDto
import com.radovan.spring.entity.RoleEntity
import com.radovan.spring.repository.RoleRepository
import com.radovan.spring.service.RoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util
import java.util.Optional

@Service
@Transactional class RoleServiceImpl extends RoleService {

  @Autowired
  private val roleRepository: RoleRepository = null

  @Autowired
  private val tempConverter: TempConverter = null

  override def listAllAuthorities: util.List[RoleDto] = {
    val allRoles: util.List[RoleEntity] = roleRepository.findAll
    val returnValue: util.List[RoleDto] = new util.ArrayList[RoleDto]
    allRoles.forEach((role: RoleEntity) => {
      def foo(role: RoleEntity) = {
        val roleDto: RoleDto = tempConverter.roleEntityToDto(role)
        returnValue.add(roleDto)
      }

      foo(role)
    })
    returnValue
  }

  override def getRoleById(id: Integer): RoleDto = {
    val roleOpt: Optional[RoleEntity] = roleRepository.findById(id)
    var returnValue: RoleDto = null
    if (roleOpt.isPresent) returnValue = tempConverter.roleEntityToDto(roleOpt.get)
    returnValue
  }
}

