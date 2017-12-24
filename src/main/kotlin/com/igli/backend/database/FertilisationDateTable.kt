package com.igli.backend.database

import com.igli.backend.models.DateDetailModel
import com.igli.backend.models.FertilisationDateModel
import com.igli.backend.models.QueryResponse
import com.mongodb.BasicDBObject
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import org.springframework.stereotype.Component
import java.util.ArrayList
import java.util.HashMap

@Component
class FertilisationDateTable {

    private var uri = MongoClientURI("mongodb://igliiDBuser:iglii@ds131237.mlab.com:31237/iglii_database")
    private var mongoClient = MongoClient(uri)
    private var igliiDatabase = mongoClient.getDB("iglii_database")
    private var fertilisationDateCollection = igliiDatabase.getCollection("FertilisationDateCollection")

    private fun getMap(womenPseudo: String, datesDetails: ArrayList<BasicDBObject>) : Map<String, Any>{
        val map = HashMap<String, Any>()
        map.put("womenPseudo", womenPseudo!!)
        map.put("datesDetails", datesDetails)
        return  map
    }

    private fun getMap(detailModel: DateDetailModel) : Map<String, Any>{
        val map = HashMap<String, Any>()
        map.put("date", detailModel.date!!)
        map.put("ovulationDate", detailModel.ovulationDate)
        map.put("dateToStartNewCycle", detailModel.dateToStartNewCycle)
        return  map
    }

    fun addFertilisationDate(fertilisationDateModel: FertilisationDateModel) : QueryResponse {
        val detailDateList = arrayListOf<BasicDBObject>()
        fertilisationDateModel.datesDetails.mapTo(detailDateList) { BasicDBObject(getMap(DateDetailModel(it.date, it.ovulationDate, it.dateToStartNewCycle))) }
        try {
            fertilisationDateCollection
                    .insert(BasicDBObject(getMap(fertilisationDateModel.womenPseudo!!.toLowerCase(), detailDateList)))
            return QueryResponse(true)
        } catch (e: Exception){
            return QueryResponse(false)
        }
    }

    fun getAllFertilisationDate() : ArrayList<FertilisationDateModel> {
        val fertilisationDateList = arrayListOf<FertilisationDateModel>()
        val cursor = fertilisationDateCollection.find()
        cursor.use { cursor ->
            while (cursor.hasNext()) {
                val obj = cursor.next()
                val datesDetails = arrayListOf<DateDetailModel>()
                val datesBDObject = obj["datesDetails"] as ArrayList<BasicDBObject>

                datesBDObject.mapIndexedTo(datesDetails) { index, i ->
                    DateDetailModel(
                            i.getString("date"),
                            i.getBoolean("ovulationDate"),
                            i.getBoolean("dateToStartNewCycle"))
                }
                fertilisationDateList.add(FertilisationDateModel(obj["womenPseudo"] as String, datesDetails))
            }
        }
        return fertilisationDateList
    }

    fun getOneFertilisationDateInfo(womenPseudo: String) : Any {
        return getAllFertilisationDate().firstOrNull {
            (it.womenPseudo.equals(womenPseudo))
        } ?: FertilisationDateModel()
    }

    fun updateFertilisationDate(fertilisationDateModel: FertilisationDateModel) : QueryResponse {
        val detailDateList = arrayListOf<BasicDBObject>()
        fertilisationDateModel.datesDetails.mapTo(detailDateList) { BasicDBObject(getMap(DateDetailModel(it.date, it.ovulationDate, it.dateToStartNewCycle))) }
        val newDocument = BasicDBObject(getMap(fertilisationDateModel.womenPseudo!!.toLowerCase(), detailDateList))
        val searchQuery = BasicDBObject().append("womenPseudo", fertilisationDateModel.womenPseudo!!.toLowerCase())
        try {
            fertilisationDateCollection.update(searchQuery, newDocument)
            return QueryResponse(true)
        } catch (e: Exception){
            return QueryResponse(false)
        }
    }

    fun deleteFertilisationDate(womenPseudo: String) : QueryResponse {
        val query = BasicDBObject()
        query.append("womenPseudo", womenPseudo.toLowerCase())
        try {
            fertilisationDateCollection.remove(query)
            return QueryResponse(true)
        } catch (e: Exception){
            return QueryResponse(false)
        }
    }
}