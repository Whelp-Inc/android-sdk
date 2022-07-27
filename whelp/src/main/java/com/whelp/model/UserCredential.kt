package com.whelp.model

data class UserCredentials(
    val language: String,
    val contact: Contact,
    val team_name: String? = null,
)

data class Contact(
    val email: String,
    val fullname: String,
    val phone: String
)