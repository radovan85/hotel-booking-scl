package com.radovan.spring.repository

import com.radovan.spring.entity.NoteEntity
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

import java.sql.Timestamp
import java.util

@Repository
trait NoteRepository extends JpaRepository[NoteEntity, Integer] {

  @Query(value = "select * from notes where created >= :ts1 and created <= :ts2", nativeQuery = true)
  def listAllForToday(@Param("ts1") ts1: Timestamp, @Param("ts2") ts2: Timestamp): util.List[NoteEntity]
}