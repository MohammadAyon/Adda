package com.example.adda.models

import java.io.Serializable

class BasicInfo : Serializable{
    var uid: String? = null
    var name: String? = null

    constructor() {}
    constructor(name: String?) {
        this.name = name
    }

    constructor(uid: String?, name: String?) {
        this.uid = uid
        this.name = name
    }
}