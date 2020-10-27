package com.example.tabi_tabi.model

class TimeLineModel {
    private var born: Number? = null
    var first: String? = null
        private set
    var last: String? = null
        private set


    constructor()
    constructor(
        born: Number?,
        first: String?,
        last: String?
    ) {
        this.first = first
        this.last = last
    }
}