package com.example.adda.models

import java.io.Serializable

class GoogleInfo : Serializable {
    var uId: String? = null
    var name: String? = null
    var email: String? = null
    var imgUrl: String? = null

    constructor() {}
    constructor(uId: String?, name: String?, email: String?, imgUrl: String?) {
        this.uId = uId
        this.name = name
        this.email = email
        this.imgUrl = imgUrl
    }
}