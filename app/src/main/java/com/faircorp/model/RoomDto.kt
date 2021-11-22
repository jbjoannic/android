package com.faircorp.model

data class RoomDto(val id: Long?, val name: String, val currentTemperature: Double?, val targetTemperature: Double?, val floor: Long?, val buildingName: String?, val buildingId: Long?) {
}