package com.example.adda.models

import java.io.Serializable

class UserInfo : Serializable {
    var uid: String? = null
    var phone: String? = null
    var name: String? = null
    var email: String? = null
    var imageUri: String? = null
    var isActiveStatus = false
    var gender: String? = null
    var department: String? = null
    var movie: ArrayList<BasicInfo>? = null
    var music: ArrayList<BasicInfo>? = null
    var sports: ArrayList<BasicInfo>? = null

    constructor() {}
    constructor(
        uid: String?,
        phone: String?,
        name: String?,
        email: String?,
        imageUri: String?,
        activeStatus: Boolean,
        gender: String?,
        department: String?,
        movie: ArrayList<BasicInfo>?,
        music: ArrayList<BasicInfo>?,
        sports: ArrayList<BasicInfo>?
    ) {
        this.uid = uid
        this.phone = phone
        this.name = name
        this.email = email
        this.imageUri = imageUri
        isActiveStatus = activeStatus
        this.gender = gender
        this.department = department
        this.movie = movie
        this.music = music
        this.sports = sports
    }
}