package com.radovan.spring.controller

import com.radovan.spring.dto.{GuestDto, NoteDto, ReservationDto, RoomCategoryDto, RoomDto, UserDto}
import com.radovan.spring.service.{GuestService, NoteService, ReservationService, RoomCategoryService, RoomService, UserService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{GetMapping, ModelAttribute, PathVariable, PostMapping, RequestMapping}

import java.time.format.DateTimeFormatter
import java.util
import scala.collection.JavaConverters._

@Controller
@RequestMapping(value = Array("/admin")) class AdminController {

  @Autowired
  private val guestService: GuestService = null

  @Autowired
  private val userService: UserService = null

  @Autowired
  private val roomCategoryService: RoomCategoryService = null

  @Autowired
  private val roomService: RoomService = null

  @Autowired
  private val noteService: NoteService = null

  @Autowired
  private val reservationService: ReservationService = null

  @GetMapping(value = Array("/allGuests")) def allGuests(map: ModelMap): String = {
    val allUsers: util.List[UserDto] = userService.listAllUsers
    val allGuests: util.List[GuestDto] = guestService.listAll
    map.put("allUsers", allUsers)
    map.put("allGuests", allGuests)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/guestList :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/guestDetails/{guestId}"))
  def getGuestDetails(@PathVariable("guestId") guestId: Integer, map: ModelMap): String = {
    val guest: GuestDto = guestService.getGuestById(guestId)
    val user: UserDto = userService.getUserById(guest.getUserId)
    map.put("user", user)
    map.put("guest", guest)
    "fragments/guestDetails :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/deleteGuest/{guestId}"))
  def deleteGuest(@PathVariable("guestId") guestId: Integer): String = {
    val guest: GuestDto = guestService.getGuestById(guestId)
    val allReservations: util.List[ReservationDto] = reservationService.listAllByGuestId(guestId)
    for (reservation <- allReservations.asScala) {
      reservationService.deleteReservation(reservation.getReservationId)
    }
    guestService.deleteGuest(guestId)
    userService.deleteUser(guest.getUserId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/allRoomCategories"))
  def listAllCategories(map: ModelMap): String = {
    val allCategories: util.List[RoomCategoryDto] = roomCategoryService.listAll
    map.put("allCategories", allCategories)
    map.put("recordsPerPage", 5.asInstanceOf[Integer])
    "fragments/roomCategoryList :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/createRoomCategory"))
  def renderCategoryForm(map: ModelMap): String = {
    val roomCategory: RoomCategoryDto = new RoomCategoryDto
    map.put("roomCategory", roomCategory)
    "fragments/roomCategoryForm :: ajaxLoadedContent"
  }

  @PostMapping(value = Array("/createRoomCategory"))
  def storeRoomCategory(@ModelAttribute("roomCategory") roomCategory: RoomCategoryDto): String = {
    roomCategoryService.addCategory(roomCategory)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/categoryDetails/{categoryId}"))
  def getCategoryDetails(@PathVariable("categoryId") categoryId: Integer, map: ModelMap): String = {
    val roomCategory: RoomCategoryDto = roomCategoryService.getCategoryById(categoryId)
    map.put("roomCategory", roomCategory)
    "fragments/roomCategoryDetails :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/updateRoomCategory/{categoryId}"))
  def renderCategoryUpdateForm(@PathVariable("categoryId") categoryId: Integer, map: ModelMap): String = {
    val roomCategory: RoomCategoryDto = new RoomCategoryDto
    val currentCategory: RoomCategoryDto = roomCategoryService.getCategoryById(categoryId)
    map.put("roomCategory", roomCategory)
    map.put("currentCategory", currentCategory)
    "fragments/updateRoomCategory :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/deleteRoomCategory/{categoryId}"))
  def deleteCategory(@PathVariable("categoryId") categoryId: Integer): String = {
    val allRooms: util.List[RoomDto] = roomService.listAllByCategoryId(categoryId)
    for (room <- allRooms.asScala) {
      reservationService.deleteAllByRoomId(room.getRoomId)
    }
    roomService.deleteAllByCategoryId(categoryId)
    roomCategoryService.deleteCategory(categoryId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/allRooms"))
  def listAllRooms(map: ModelMap): String = {
    val allRooms: util.List[RoomDto] = roomService.listAll
    val allCategories: util.List[RoomCategoryDto] = roomCategoryService.listAll
    map.put("allRooms", allRooms)
    map.put("allCategories", allCategories)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/roomList :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/createRoom"))
  def renderRoomForm(map: ModelMap): String = {
    val room: RoomDto = new RoomDto
    val allCategories: util.List[RoomCategoryDto] = roomCategoryService.listAll
    map.put("room", room)
    map.put("allCategories", allCategories)
    "fragments/roomForm :: ajaxLoadedContent"
  }

  @PostMapping(value = Array("/createRoom"))
  def storeRoom(@ModelAttribute("room") room: RoomDto): String = {
    roomService.addRoom(room)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/roomDetails/{roomId}"))
  def getRoomDetails(@PathVariable("roomId") roomId: Integer, map: ModelMap): String = {
    val room: RoomDto = roomService.getRoomById(roomId)
    val category: RoomCategoryDto = roomCategoryService.getCategoryById(room.getRoomCategoryId)
    map.put("room", room)
    map.put("category", category)
    "fragments/roomDetails :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/updateRoom/{roomId}"))
  def renderRoomUpdateForm(@PathVariable("roomId") roomId: Integer, map: ModelMap): String = {
    val room: RoomDto = new RoomDto
    val currentRoom: RoomDto = roomService.getRoomById(roomId)
    val allCategories: util.List[RoomCategoryDto] = roomCategoryService.listAll
    map.put("room", room)
    map.put("currentRoom", currentRoom)
    map.put("allCategories", allCategories)
    "fragments/updateRoom :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/deleteRoom/{roomId}"))
  def deleteRoom(@PathVariable("roomId") roomId: Integer): String = {
    reservationService.deleteAllByRoomId(roomId)
    roomService.deleteRoom(roomId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/allNotes"))
  def listAllNotes(map: ModelMap): String = {
    val allNotes: util.List[NoteDto] = noteService.listAll
    map.put("allNotes", allNotes)
    map.put("recordsPerPage", 10.asInstanceOf[Integer])
    "fragments/noteList :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/allNotesToday"))
  def listAllNotesForToday(map: ModelMap): String = {
    val allNotes: util.List[NoteDto] = noteService.listAllForToday
    map.put("allNotes", allNotes)
    map.put("recordsPerPage", 10.asInstanceOf[Integer])
    "fragments/noteList :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/noteDetails/{noteId}"))
  def getNoteDetails(@PathVariable("noteId") noteId: Integer, map: ModelMap): String = {
    val note: NoteDto = noteService.getNoteById(noteId)
    map.put("note", note)
    "fragments/noteDetails :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/deleteNote/{noteId}"))
  def deleteNote(@PathVariable("noteId") noteId: Integer): String = {
    noteService.deleteNote(noteId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/deleteAllNotes"))
  def deleteAllNotes(): String = {
    noteService.deleteAllNotes()
    "fragments/homePage :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/allReservations"))
  def listAllReservations(map: ModelMap): String = {
    val allReservations: util.List[ReservationDto] = reservationService.listAll
    val allGuests: util.List[GuestDto] = guestService.listAll
    val allUsers: util.List[UserDto] = userService.listAllUsers
    map.put("allReservations", allReservations)
    map.put("allGuests", allGuests)
    map.put("allUsers", allUsers)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/reservationList :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/allActiveReservations"))
  def listAllActiveReservations(map: ModelMap): String = {
    val allReservations: util.List[ReservationDto] = reservationService.listAllActive
    val allGuests: util.List[GuestDto] = guestService.listAll
    val allUsers: util.List[UserDto] = userService.listAllUsers
    map.put("allReservations", allReservations)
    map.put("allGuests", allGuests)
    map.put("allUsers", allUsers)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/reservationList :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/allExpiredReservations"))
  def listAllExpiredReservations(map: ModelMap): String = {
    val allReservations: util.List[ReservationDto] = reservationService.listAllExpired
    val allGuests: util.List[GuestDto] = guestService.listAll
    val allUsers: util.List[UserDto] = userService.listAllUsers
    map.put("allReservations", allReservations)
    map.put("allGuests", allGuests)
    map.put("allUsers", allUsers)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/reservationList :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/reservationDetails/{reservationId}"))
  def getReservationDetails(@PathVariable("reservationId") reservationId: Integer, map: ModelMap): String = {
    val reservation: ReservationDto = reservationService.getReservationById(reservationId)
    val room: RoomDto = roomService.getRoomById(reservation.getRoomId)
    val category: RoomCategoryDto = roomCategoryService.getCategoryById(room.getRoomCategoryId)
    val guest: GuestDto = guestService.getGuestById(reservation.getGuestId)
    val user: UserDto = userService.getUserById(guest.getUserId)
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val createdAtStr: String = reservation.getCreatedAt.toLocalDateTime.format(formatter)
    val updatedAtStr: String = reservation.getUpdatedAt.toLocalDateTime.format(formatter)
    map.put("room", room)
    map.put("category", category)
    map.put("reservation", reservation)
    map.put("user", user)
    map.put("createdAtStr", createdAtStr)
    map.put("updatedAtStr", updatedAtStr)
    "fragments/reservationDetails :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/switchReservationRoom/{reservationId}"))
  def changeReservationRoom(@PathVariable("reservationId") reservationId: Integer, map: ModelMap): String = {
    val reservation: ReservationDto = new ReservationDto
    val currentReservation: ReservationDto = reservationService.getReservationById(reservationId)
    val currentRoom: RoomDto = roomService.getRoomById(currentReservation.getRoomId)
    val availableRooms: util.List[RoomDto] = roomService.listAllByCategoryId(currentRoom.getRoomCategoryId)
    val category: RoomCategoryDto = roomCategoryService.getCategoryById(currentRoom.getRoomCategoryId)
    availableRooms.removeIf((obj: RoomDto) => obj.getRoomId eq currentRoom.getRoomId)
    map.put("reservation", reservation)
    map.put("currentReservation", currentReservation)
    map.put("currentRoom", currentRoom)
    map.put("availableRooms", availableRooms)
    map.put("category", category)
    "fragments/switchRoomForm :: ajaxLoadedContent"
  }

  @PostMapping(value = Array("/updateReservation/{reservationId}"))
  def updateReservation(@ModelAttribute reservation: ReservationDto, @PathVariable("reservationId") reservationId: Integer): String = {
    reservationService.updateReservation(reservation, reservationId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/deleteReservation/{reservationId}"))
  def deleteReservation(@PathVariable("reservationId") reservationId: Integer): String = {
    reservationService.deleteReservation(reservationId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/roomNumberError"))
  def roomError: String = "fragments/roomNumberError :: ajaxLoadedContent"
}

