package com.radovan.spring.service

import com.radovan.spring.dto.NoteDto

import java.util

trait NoteService {

  def getNoteById(noteId: Integer): NoteDto

  def deleteNote(noteId: Integer): Unit

  def listAll: util.List[NoteDto]

  def listAllForToday: util.List[NoteDto]

  def deleteAllNotes(): Unit
}
