package com.igli.backend.database

import com.igli.backend.models.FertilisationTextToNotifModel
import com.igli.backend.models.QueryResponse
import com.mongodb.BasicDBObject
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import org.springframework.stereotype.Component

@Component
class FertilisationTextToNotifTable {

    private var uri = MongoClientURI("mongodb://igliiDBuser:iglii@ds131237.mlab.com:31237/iglii_database")
    private var mongoClient = MongoClient(uri)
    private var igliiDatabase = mongoClient.getDB("iglii_database")
    private var fertilisationTexTotNotifCollection = igliiDatabase.getCollection("FertilisationTextToNotifCollection")

    private fun getMap(fertilisationTextToNotif: FertilisationTextToNotifModel) : Map<String, Any>{
        val map = HashMap<String, Any>()
        map.put("id", fertilisationTextToNotif.id!!)
        map.put("wantBePregnant", fertilisationTextToNotif.wantBePregnant)
        map.put("isWomen", fertilisationTextToNotif.isWomen)
        map.put("isOvulationDate", fertilisationTextToNotif.isOvulationDate)
        map.put("isDateToStartNewCycle", fertilisationTextToNotif.isDateToStartNewCycle)
        map.put("notifText", fertilisationTextToNotif.notifText!!)
        map.put("normalText", fertilisationTextToNotif.normalText!!)
        map.put("pourcentage", fertilisationTextToNotif.pourcentage!!)
        return  map
    }

    fun addFertilisationTextToNotif(fertilisationTextToNotif: FertilisationTextToNotifModel) : QueryResponse {
        try {
            fertilisationTexTotNotifCollection.insert(BasicDBObject(getMap(fertilisationTextToNotif)))
            return QueryResponse(true)
        } catch (e: Exception){
            return QueryResponse(false)
        }
    }

    fun getAllFertilisationTextToNotif() : ArrayList<FertilisationTextToNotifModel> {
        val list = arrayListOf<FertilisationTextToNotifModel>()
        val cursor = fertilisationTexTotNotifCollection.find()
        cursor.use { cursor ->
            while (cursor.hasNext()) {
                val obj = cursor.next()
                list.add(FertilisationTextToNotifModel(
                        obj["id"] as Int,
                        obj["wantBePregnant"] as Boolean,
                        obj["isWomen"] as Boolean,
                        obj["isOvulationDate"] as Boolean,
                        obj["isDateToStartNewCycle"] as Boolean,
                        obj["notifText"] as String,
                        obj["normalText"] as String,
                        obj["pourcentage"] as String))
            }
        }
        return list
    }

    fun getOneFertilisationTextToNotif(wantBePregnant: Boolean,
                    isWomen: Boolean,
                    isOvulationDate: Boolean,
                    isDateToStartNewCycle: Boolean) : FertilisationTextToNotifModel {
        return getAllFertilisationTextToNotif().firstOrNull {
            ((it.wantBePregnant == wantBePregnant)
                    && (it.isWomen == isWomen)
                    && (it.isOvulationDate == isOvulationDate)
                    && (it.isDateToStartNewCycle == isDateToStartNewCycle))
        } ?: FertilisationTextToNotifModel()
    }

    fun updateFertilisationTextToNotif(fertilisationTextToNotif: FertilisationTextToNotifModel) : QueryResponse {
        val newDocument = BasicDBObject(getMap(fertilisationTextToNotif))
        val searchQuery = BasicDBObject().append("id", fertilisationTextToNotif.id)
        try {
            fertilisationTexTotNotifCollection.update(searchQuery, newDocument)
            return QueryResponse(true)
        } catch (e: Exception){
            return QueryResponse(false)
        }
    }

    fun deleteFertilisationTextToNotif(id: Int) : QueryResponse {
        val query = BasicDBObject()
        query.append("id", id)
        try {
            fertilisationTexTotNotifCollection.remove(query)
            return QueryResponse(true)
        } catch (e: Exception){
            return QueryResponse(false)
        }
    }
}