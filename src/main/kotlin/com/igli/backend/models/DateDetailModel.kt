package com.igli.backend.models

class DateDetailModel {

    var date: String? = null
    var ovulationDate: Boolean = false
    var dateToStartNewCycle: Boolean = false

    constructor()

    constructor(date: String?, isOvulationDate: Boolean, isDateToStartNewCycle: Boolean) {
        this.date = date
        this.ovulationDate = isOvulationDate
        this.dateToStartNewCycle = isDateToStartNewCycle
    }

    override fun toString(): String {
        return "{date=$date, ovulationDate=$ovulationDate, dateToStartNewCycle=$dateToStartNewCycle}"
    }
}