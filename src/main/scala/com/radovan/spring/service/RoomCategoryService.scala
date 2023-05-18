package com.radovan.spring.service

import com.radovan.spring.dto.RoomCategoryDto

import java.util

trait RoomCategoryService {

  def addCategory(category: RoomCategoryDto): RoomCategoryDto

  def getCategoryById(categoryId: Integer): RoomCategoryDto

  def deleteCategory(categoryId: Integer): Unit

  def listAll: util.List[RoomCategoryDto]
}
