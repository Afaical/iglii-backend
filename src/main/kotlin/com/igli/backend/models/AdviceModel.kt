package com.igli.backend.models

class AdviceModel {

    var adviceId: Int? = null
    var adviceWomenWantBePregnant: Boolean = false
    var adviceText: String? = null

    constructor()

    constructor(adviceId: Int?, adviceWomenWantBePregnant: Boolean, adviceText: String?) {
        this.adviceId = adviceId
        this.adviceWomenWantBePregnant = adviceWomenWantBePregnant
        this.adviceText = adviceText
    }
}