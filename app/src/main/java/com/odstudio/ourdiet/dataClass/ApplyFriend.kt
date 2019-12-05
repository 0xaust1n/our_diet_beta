package com.odstudio.ourdiet.dataClass

data class ApplyFriend(
    var nick: String = "",
    var uid: String = "",
    var avatar: String = "",
    var email: String = "",
    val status: String = ""

)

data class ReceiveApply(
    var nick: String = "",
    var uid: String = "",
    var avatar: String = "",
    var email: String = "",
    val status: String = ""
)

data class UserInfo(
    var userInfoList: ArrayList<UserItem> = arrayListOf()
)

data class UserItem(
    var nick: String = "",
    var uid: String = "",
    var avatar: String = "",
    var email: String = "",
    val status: String = ""
)