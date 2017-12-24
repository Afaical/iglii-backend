package com.igli.backend.models

class FCMSendModel {

    var to: String? = null
    var title: String? = null
    var body: String? = null
    var subText: String? = null
    var clickAction: String? = null

    constructor()

    constructor(to: String?, title: String?, body: String?, subText: String?, clickAction: String?) {
        this.to = to
        this.title = title
        this.body = body
        this.subText = subText
        this.clickAction = clickAction
    }

    override fun toString(): String {
        return "FCMSendModel(to=$to, title=$title, body=$body, subText=$subText, clickAction=$clickAction)"
    }


//    constructor(to: String?, title: String?, body: String?, subText: String?) {
//        this.to = to
//        this.title = title
//        this.body = body
//        this.subText = subText
//    }

//    override fun toString(): String {
//        return "FCMSendModel{to=$to, title=$title, body=$body, subText=$subText}"
//    }
}