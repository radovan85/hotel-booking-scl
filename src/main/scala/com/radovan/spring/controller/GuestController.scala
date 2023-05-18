package com.radovan.spring.controller

import com.radovan.spring.dto.{GuestDto, ReservationDto, RoomCategoryDto, RoomDto, UserDto}
import com.radovan.spring.service.{GuestService, ReservationService, RoomCategoryService, RoomService, UserService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{GetMapping, ModelAttribute, PathVariable, PostMapping, RequestMapping}

import java.sql.Timestamp
import java.time.{LocalDate, LocalDateTime}
import java.time.format.DateTimeFormatter
import java.util
import scala.collection.JavaConverters._
import scala.util.control.Breaks._

@Controller
@RequestMapping(value = Array("/guests")) class GuestController {

  @Autowired
  private val userService: UserService = null

  @Autowired
  private val guestService: GuestService = null

  @Autowired
  private val reservationService: ReservationService = null

  @Autowired
  private val roomCategoryService: RoomCategoryService = null

  @Autowired
  private val roomService: RoomService = null

  @GetMapping(value = Array("/allUserReservations"))
  def getAllUserReservations(map: ModelMap): String = {
    val user: UserDto = userService.getCurrentUser
    val guest: GuestDto = guestService.getGuestByUserId(user.getId)
    val allReservations: util.List[ReservationDto] = reservationService.listAllByGuestId(guest.getGuestId)
    val allRooms: util.List[RoomDto] = roomService.listAll
    val allCategories: util.List[RoomCategoryDto] = roomCategoryService.listAll
    map.put("allReservations", allReservations)
    map.put("allRooms", allRooms)
    map.put("allCategories", allCategories)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/userReservationList :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/createReservation/{checkInDateStr}/{numberOfNights}"))
  def renderReservationForm(@PathVariable("checkInDateStr") checkInDateStr: String, @PathVariable("numberOfNights") numberOfNights: Integer, map: ModelMap): String = {
    val reservation: ReservationDto = new ReservationDto
    val user: UserDto = userService.getCurrentUser
    val guest: GuestDto = guestService.getGuestByUserId(user.getId)
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val checkInDateStrVar = checkInDateStr + " 14:00"
    val allCategories: util.List[RoomCategoryDto] = roomCategoryService.listAll
    val availableRooms: util.List[RoomDto] = new util.ArrayList[RoomDto]
    val checkInDate: LocalDateTime = LocalDateTime.parse(checkInDateStrVar, formatter)
    var checkOutDate: LocalDateTime = checkInDate
    checkOutDate = checkOutDate.plusDays(numberOfNights.longValue())
    checkOutDate = checkOutDate.minusHours(2)
    val checkOutDateStr: String = checkOutDate.format(formatter)
    val checkInStamp: Timestamp = Timestamp.valueOf(checkInDate)
    val checkOutStamp: Timestamp = Timestamp.valueOf(checkOutDate)

    for (category <- allCategories.asScala) {
      val rooms: util.List[RoomDto] = roomService.listAllByCategoryId(category.getRoomCategoryId)
      breakable{
        for (roomDto <- rooms.asScala) {
          if (reservationService.isAvailable(roomDto.getRoomId, checkInStamp, checkOutStamp)) {
            availableRooms.add(roomDto)
            break
          }
        }
      }
    }
    map.put("checkInDateStr", checkInDateStrVar)
    map.put("checkOutDateStr", checkOutDateStr)
    map.put("checkInDate", checkInDate)
    map.put("checkOutDate", checkOutDate)
    map.put("numberOfNights", numberOfNights)
    map.put("allCategories", allCategories)
    map.put("availableRooms", availableRooms)
    map.put("guestId", guest.getGuestId)
    map.put("reservation", reservation)
    "fragments/reservationForm :: ajaxLoadedContent"
  }

  @PostMapping(value = Array("/createReservation/{roomId}"))
  def storeReservation(@ModelAttribute("reservation") reservation: ReservationDto, @PathVariable("roomId") roomId: Integer): String = {
    reservationService.addReservation(reservation, roomId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/bookReservation"))
  def bookReservation(map: ModelMap): String = {
    val reservation: ReservationDto = new ReservationDto
    val today: LocalDate = LocalDate.now
    val maxDate: LocalDate = today.plusYears(1)
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val todayStr: String = today.format(formatter)
    val maxDateStr: String = maxDate.format(formatter)
    map.put("reservation", reservation)
    map.put("todayStr", todayStr)
    map.put("maxDateStr", maxDateStr)
    "fragments/reservationBooking :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/deleteReservation/{reservationId}"))
  def deleteReservation(@PathVariable("reservationId") reservationId: Integer): String = {
    reservationService.deleteReservation(reservationId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/accountDetails"))
  def getAccountDetails(map: ModelMap): String = {
    val authUser: UserDto = userService.getCurrentUser
    val guest: GuestDto = guestService.getGuestByUserId(authUser.getId)
    map.put("guest", guest)
    map.put("user", authUser)
    "fragments/guestDetails :: ajaxLoadedContent"
  }
}

