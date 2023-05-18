package com.radovan.spring.service.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.RoomCategoryDto
import com.radovan.spring.entity.RoomCategoryEntity
import com.radovan.spring.repository.RoomCategoryRepository
import com.radovan.spring.service.RoomCategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util
import java.util.Optional

@Service
@Transactional
class RoomCategoryServiceImpl extends RoomCategoryService {

  @Autowired
  private val categoryRepository: RoomCategoryRepository = null

  @Autowired
  private val tempConverter: TempConverter = null

  override def addCategory(category: RoomCategoryDto): RoomCategoryDto = {
    val categoryEntity: RoomCategoryEntity = tempConverter.roomCategoryDtoToEntity(category)
    val storedCategory: RoomCategoryEntity = categoryRepository.save(categoryEntity)
    val returnValue: RoomCategoryDto = tempConverter.roomCategoryEntityToDto(storedCategory)
    returnValue
  }

  override def getCategoryById(categoryId: Integer): RoomCategoryDto = {
    var returnValue: RoomCategoryDto = null
    val categoryOpt: Optional[RoomCategoryEntity] = categoryRepository.findById(categoryId)
    if (categoryOpt.isPresent) returnValue = tempConverter.roomCategoryEntityToDto(categoryOpt.get)
    returnValue
  }

  override def deleteCategory(categoryId: Integer): Unit = {
    categoryRepository.deleteById(categoryId)
    categoryRepository.flush()
  }

  override def listAll: util.List[RoomCategoryDto] = {
    val returnValue: util.List[RoomCategoryDto] = new util.ArrayList[RoomCategoryDto]
    val allCategoriesOpt: Optional[util.List[RoomCategoryEntity]] = Optional.ofNullable(categoryRepository.findAll)
    if (!allCategoriesOpt.isEmpty) allCategoriesOpt.get.forEach((category: RoomCategoryEntity) => {
      def foo(category: RoomCategoryEntity) = {
        val categoryDto: RoomCategoryDto = tempConverter.roomCategoryEntityToDto(category)
        returnValue.add(categoryDto)
      }

      foo(category)
    })
    returnValue
  }
}

