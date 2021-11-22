package com.faircorp.model

enum class WindowStatus { OPEN, CLOSED }
data class WindowDto(
    val id: Long?,
    val name: String,
    val roomName: String?,
    val roomId: Long?,
    val windowStatus: WindowStatus?
) {
}