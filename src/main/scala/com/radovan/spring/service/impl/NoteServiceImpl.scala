package com.radovan.spring.service.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.NoteDto
import com.radovan.spring.entity.NoteEntity
import com.radovan.spring.repository.NoteRepository
import com.radovan.spring.service.NoteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.sql.Timestamp
import java.time.{LocalDate, LocalDateTime}
import java.time.format.DateTimeFormatter
import java.util
import java.util.Optional

@Service
@Transactional class NoteServiceImpl extends NoteService {

  @Autowired
  private val noteRepository: NoteRepository = null

  @Autowired
  private val tempConverter: TempConverter = null

  override def getNoteById(noteId: Integer): NoteDto = {
    var returnValue: NoteDto = null
    val noteOpt: Optional[NoteEntity] = noteRepository.findById(noteId)
    if (noteOpt.isPresent) returnValue = tempConverter.noteEntityToDto(noteOpt.get)
    returnValue
  }

  override def deleteNote(noteId: Integer): Unit = {
    noteRepository.deleteById(noteId)
    noteRepository.flush()
  }

  override def listAll: util.List[NoteDto] = {
    val returnValue: util.List[NoteDto] = new util.ArrayList[NoteDto]
    val allNotesOpt: Optional[util.List[NoteEntity]] = Optional.ofNullable(noteRepository.findAll)
    if (!allNotesOpt.isEmpty) allNotesOpt.get.forEach((note: NoteEntity) => {
      def foo(note: NoteEntity) = {
        val noteDto: NoteDto = tempConverter.noteEntityToDto(note)
        returnValue.add(noteDto)
      }

      foo(note)
    })
    returnValue
  }

  override def listAllForToday: util.List[NoteDto] = {
    val returnValue: util.List[NoteDto] = new util.ArrayList[NoteDto]
    val currentDate: LocalDate = LocalDate.now
    var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    var timestamp1Str: String = currentDate.format(formatter)
    timestamp1Str = timestamp1Str + " 00:00:00"
    var timestamp2Str: String = currentDate.format(formatter)
    timestamp2Str = timestamp2Str + " 23:59:59"
    formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val dateTime1: LocalDateTime = LocalDateTime.parse(timestamp1Str, formatter)
    val timestamp1: Timestamp = Timestamp.valueOf(dateTime1)
    val dateTime2: LocalDateTime = LocalDateTime.parse(timestamp2Str, formatter)
    val timestamp2: Timestamp = Timestamp.valueOf(dateTime2)
    val allNotesOpt: Optional[util.List[NoteEntity]] = Optional.ofNullable(noteRepository.listAllForToday(timestamp1, timestamp2))
    if (!allNotesOpt.isEmpty) allNotesOpt.get.forEach((note: NoteEntity) => {
      def foo(note: NoteEntity) = {
        val noteDto: NoteDto = tempConverter.noteEntityToDto(note)
        returnValue.add(noteDto)
      }

      foo(note)
    })
    returnValue
  }

  override def deleteAllNotes(): Unit = {
    noteRepository.deleteAll()
    noteRepository.flush()
  }
}
