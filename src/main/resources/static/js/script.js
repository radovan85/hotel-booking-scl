window.onload = redirectHome;

function redirectHome() {
	$("#ajaxLoadedContent").load("/home");
}

function redirectContact() {
	$("#ajaxLoadedContent").load("/contactInfo");
}

function redirectLogin() {
	$("#ajaxLoadedContent").load("/login");
}

function redirectRegister() {
	$("#ajaxLoadedContent").load("/register");
}

function redirectAllGuests() {
	$("#ajaxLoadedContent").load("/admin/allGuests");
}

function redirectGuestDetails(guestId) {
	$("#ajaxLoadedContent").load("/admin/guestDetails/" + guestId);
}

function redirectAllCategories() {
	$("#ajaxLoadedContent").load("/admin/allRoomCategories");
}

function redirectAddCategory() {
	$("#ajaxLoadedContent").load("/admin/createRoomCategory");
}

function redirectCategoryDetails(categoryId) {
	$("#ajaxLoadedContent").load("/admin/categoryDetails/" + categoryId);
}

function redirectUpdateCategory(categoryId) {
	$("#ajaxLoadedContent").load("/admin/updateRoomCategory/" + categoryId);
}

function deleteCategory(categoryId) {
	$("#ajaxLoadedContent").load("/admin/deleteRoomCategory" + categoryId);
}

function redirectAllRooms() {
	$("#ajaxLoadedContent").load("/admin/allRooms");
}

function redirectAddRoom() {
	$("#ajaxLoadedContent").load("/admin/createRoom");
}

function redirectRoomDetails(roomId) {
	$("#ajaxLoadedContent").load("/admin/roomDetails/" + roomId);
}

function redirectUpdateRoom(roomId) {
	$("#ajaxLoadedContent").load("/admin/updateRoom/" + roomId);
}

function redirectUserReservations() {
	$("#ajaxLoadedContent").load("/guests/allUserReservations");
}

function redirectAllReservations() {
	$("#ajaxLoadedContent").load("/admin/allReservations");
}

function redirectAllActiveReservations() {
	$("#ajaxLoadedContent").load("/admin/allActiveReservations");
}

function redirectAllExpiredReservations() {
	$("#ajaxLoadedContent").load("/admin/allExpiredReservations");
}

function redirectReservationDetails(reservationId) {
	$("#ajaxLoadedContent").load("/admin/reservationDetails/" + reservationId);
}

function redirectBookReservation() {
	$("#ajaxLoadedContent").load("/guests/bookReservation");
}

function redirectAllNotes() {
	$("#ajaxLoadedContent").load("/admin/allNotes");
}

function redirectAllNotesFromToday() {
	$("#ajaxLoadedContent").load("/admin/allNotesToday");
}

function redirectNoteDetails(noteId) {
	$("#ajaxLoadedContent").load("/admin/noteDetails/" + noteId);
}

function redirectSwitchRoom(reservationId) {
	$("#ajaxLoadedContent").load(
			"/admin/switchReservationRoom/" + reservationId);
}

function redirectAccountDetails(){
	$("#ajaxLoadedContent").load("/guests/accountDetails");
}


function confirmLoginPass() {
	$.ajax({
		url : "http://localhost:8080/loginPassConfirm",
		type : "POST"
	})
	.done(function(){
		window.location.href = "/";
	})
	.fail(function(){
		$("#ajaxLoadedContent").load("/loginErrorPage");
	})
}




function redirectLogout() {
	$.ajax({
		type : "POST",
		url : "http://localhost:8080/loggedout"
	})
	.done(function(){
		window.location.href = "/";
	})
	.fail(function(){
		alert("Logout error!");
	})
}

function deleteCategory(categoryId) {
	if (confirm("Are you sure you want to clear this category?\nThis will affect all rooms and reservations related!")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteRoomCategory/" + categoryId
		})
		.done(function(){
			redirectAllCategories();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};

function deleteRoom(roomId) {
	if (confirm("Are you sure you want to clear this room?\nThis will affect all reservations related!")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteRoom/" + roomId
		})
		.done(function(){
			redirectAllRooms();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};

function deleteReservation(reservationId) {
	if (confirm("Are you sure you want to cancel this reservation?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/guests/deleteReservation/" + reservationId
		})
		.done(function(){
			redirectUserReservations();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};

function deleteReservationAdmin(reservationId) {
	if (confirm("Are you sure you want to remove this reservation?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteReservation/" + reservationId
		})
		.done(function(){
			redirectAllReservations();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};

function deleteNote(noteId) {
	if (confirm("Are you sure you want to remove this note?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteNote/" + noteId
		})
		.done(function(){
			redirectAllNotes();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};

function deleteAllNotes() {
	if (confirm("Are you sure you want to clear all notes?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteAllNotes"
		})
		.done(function(){
			redirectAllNotes();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};

function deleteGuest(guestId) {
	if (confirm("Are you sure you want to remove this guest?\nIt will affect all related reservations!")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteGuest/" + guestId
		})
		.done(function(){
			redirectAllGuests();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};

function reservationInterceptor(formName, roomId) {
	var element = document.getElementById(formName);
	element.addEventListener("submit", event => {
		event.preventDefault();
		var formData = $("#" + formName);
		$.ajax({
			url : "http://localhost:8080/guests/createReservation/" + roomId,
			type : "POST",
			data : formData.serialize()
		})
		.done(function(){
			$("#ajaxLoadedContent").load("/guests/allUserReservations");
		})
		.fail(function(){
			alert("Failed!");
		})
	})
};




