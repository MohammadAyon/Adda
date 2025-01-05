package com.example.adda.models

class Messages {
    var message: String? = null
    var senderId: String? = null
    var imageUrl: String? = null
    var timeStamp: Long = 0

    constructor() {}
    constructor(message: String?, senderId: String?, imageUrl: String?, timeStamp: Long) {
        this.message = message
        this.senderId = senderId
        this.imageUrl = imageUrl
        this.timeStamp = timeStamp
    }
}