package com.subhajit.barta.data

data class UserData(
    var userId: String ?= "" ,
    var userName: String ?= "" ,
    var userNumber: String ?= "" ,
    var userImage: String ?= ""
){
    fun toMap() = mapOf(
        "userId" to userId,
        "userName" to userName,
        "userNumber" to userNumber,
        "userImage" to userImage
    )
}
