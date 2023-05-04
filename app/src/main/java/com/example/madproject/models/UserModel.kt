package com.example.madproject.models

data class UserModel(
    var userId : String? = null,
    var name: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var password: String? = null,
    var bio : String? = null,
    var userImage : String? = null,
    var isAdmin : Boolean? = null

)
