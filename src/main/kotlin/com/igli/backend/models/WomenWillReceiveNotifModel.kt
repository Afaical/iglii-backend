package com.igli.backend.models

class WomenWillReceiveNotifModel {

    var womenPseudo: String? = null
    var dateDetail: DateDetailModel? = null

    constructor()

    constructor(womenPseudo: String?, dateDetail: DateDetailModel?) {
        this.womenPseudo = womenPseudo
        this.dateDetail = dateDetail
    }

    override fun toString(): String {
        return "{womenPseudo=$womenPseudo, dateDetail=$dateDetail}"
    }
}