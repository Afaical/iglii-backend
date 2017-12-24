package com.igli.backend.database

import com.igli.backend.models.AdviceModel
import com.igli.backend.models.QueryResponse
import com.igli.backend.models.UserModel
import com.mongodb.BasicDBObject
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import org.springframework.stereotype.Component

@Component
class AdviceTable {

    private var uri = MongoClientURI("mongodb://igliiDBuser:iglii@ds131237.mlab.com:31237/iglii_database")
    private var mongoClient = MongoClient(uri)
    private var igliiDatabase = mongoClient.getDB("iglii_database")
    private var adviceCollection = igliiDatabase.getCollection("AdviceCollection")

    private fun getMap(advice: AdviceModel) : Map<String, Any>{
        val map = HashMap<String, Any>()
        map.put("adviceId", advice.adviceId!!)
        map.put("adviceWomenWantBePregnant", advice.adviceWomenWantBePregnant)
        map.put("adviceText", advice.adviceText!!)
        return  map
    }

    fun addAdvice(advice: AdviceModel) : QueryResponse {
        try {
            adviceCollection.insert(BasicDBObject(getMap(advice)))
            return QueryResponse(true)
        } catch (e: Exception){
            return QueryResponse(false)
        }
    }

    fun getAllAdvice() : ArrayList<AdviceModel> {
        val adviceList = arrayListOf<AdviceModel>()
        val cursor = adviceCollection.find()
        cursor.use { cursor ->
            while (cursor.hasNext()) {
                val obj = cursor.next()
                adviceList.add(AdviceModel(
                        obj["adviceId"] as Int,
                        obj["adviceWomenWantBePregnant"] as Boolean,
                        obj["adviceText"] as String))
            }
        }
        return adviceList
    }

    fun getAllAdviceToBePregnant() : ArrayList<AdviceModel> {
        val adviceList = arrayListOf<AdviceModel>()
        getAllAdvice().filterTo(adviceList) { it.adviceWomenWantBePregnant }
        return adviceList
    }

    fun getAllAdviceToNotBePregnant() : ArrayList<AdviceModel> {
        val adviceList = arrayListOf<AdviceModel>()
        getAllAdvice().filterTo(adviceList) { !it.adviceWomenWantBePregnant }
        return adviceList
    }

    fun updateAdvice(advice: AdviceModel) : QueryResponse {
        val newDocument = BasicDBObject(getMap(advice))
        val searchQuery = BasicDBObject().append("adviceId", advice.adviceId)
        try {
            adviceCollection.update(searchQuery, newDocument)
            return QueryResponse(true)
        } catch (e: Exception){
            return QueryResponse(false)
        }
    }

    fun deleteAdvice(adviceId: Int) : QueryResponse {
        val query = BasicDBObject()
        query.append("adviceId", adviceId)
        try {
            adviceCollection.remove(query)
            return QueryResponse(true)
        } catch (e: Exception){
            return QueryResponse(false)
        }
    }
}