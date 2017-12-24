package com.igli.backend.database

import com.igli.backend.models.QueryResponse
import com.igli.backend.models.UserModel
import com.igli.backend.models.WomenModel
import com.mongodb.BasicDBObject
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import org.springframework.stereotype.Component
import java.util.*

@Component
class WomenTable {

    private var uri = MongoClientURI("mongodb://igliiDBuser:iglii@ds131237.mlab.com:31237/iglii_database")
    private var mongoClient = MongoClient(uri)
    private var igliiDatabase = mongoClient.getDB("iglii_database")
    private var womenCollection = igliiDatabase.getCollection("WomenCollection")

    private fun getMap(womenModel: WomenModel) : Map<String, Any>{
        val map = HashMap<String, Any>()
        map.put("womenPseudo", womenModel.womenPseudo!!)
        map.put("womenMenPseudo", womenModel.womenMenPseudo!!)
        map.put("womenWantToBePregnant", womenModel.womenWantToBePregnant)
        map.put("womenCycleDuration", womenModel.womenCycleDuration!!)
        map.put("womenDateStartActualCycle", womenModel.womenDateStartActualCycle!!)
        return  map
    }

    fun addWomenUser(womenModel: WomenModel) : QueryResponse {
        try {
            womenCollection.insert(BasicDBObject(getMap(womenModel)))
            return QueryResponse(true)
        } catch (e: Exception){
            return QueryResponse(false)
        }
    }

    fun getAllWomenUser() : ArrayList<WomenModel> {
        val womenUserList = arrayListOf<WomenModel>()
        val cursor = womenCollection.find()
        cursor.use { cursor ->
            while (cursor.hasNext()) {
                val obj = cursor.next()
                womenUserList.add(WomenModel(
                        obj["womenPseudo"] as String,
                        obj["womenMenPseudo"] as String,
                        obj["womenWantToBePregnant"] as Boolean,
                        obj["womenCycleDuration"] as Int,
                        obj["womenDateStartActualCycle"] as String))
            }
        }
        return womenUserList
    }

    fun getWomenUserInfo(womenPseudo: String) : WomenModel {
        return getAllWomenUser().firstOrNull {
            (it.womenPseudo.equals(womenPseudo))
        }?: WomenModel()
    }

    fun menUserPartnerExist(womenPseudo: String) : WomenModel {
        return getAllWomenUser().firstOrNull{
            it.womenPseudo.equals(womenPseudo)
        }?: WomenModel()
//        return if (getAllWomenUser().any { it.womenPseudo.equals(womenPseudo) }) QueryResponse(true) else return QueryResponse(false)
    }

    fun updateWomenUser(womenUser: WomenModel) : QueryResponse {
        val newDocument = BasicDBObject(getMap(womenUser))
        val searchQuery = BasicDBObject().append("womenPseudo", womenUser.womenPseudo)
        try {
            womenCollection.update(searchQuery, newDocument)
            return QueryResponse(true)
        } catch (e: Exception){
            return QueryResponse(false)
        }
    }

    fun deleteWomenUser(womenPseudo: String) : QueryResponse {
        val query = BasicDBObject()
        query.append("womenPseudo", womenPseudo)
        try {
            womenCollection.remove(query)
            return QueryResponse(true)
        } catch (e: Exception){
            return QueryResponse(false)
        }
    }
}