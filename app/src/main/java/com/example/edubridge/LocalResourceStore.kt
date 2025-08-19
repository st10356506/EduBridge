package com.example.edubridge

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

data class LocalResource(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val path: String,
    val mimeType: String,
    val timestamp: Long = System.currentTimeMillis()
)

object LocalResourceStore {

    private const val PREFS = "local_resources_prefs"
    private const val KEY = "resources_json"

    fun getAll(context: Context): List<LocalResource> {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY, "[]") ?: "[]"
        val arr = JSONArray(json)
        val list = mutableListOf<LocalResource>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            list.add(
                LocalResource(
                    id = o.getString("id"),
                    title = o.getString("title"),
                    path = o.getString("path"),
                    mimeType = o.getString("mimeType"),
                    timestamp = o.optLong("timestamp", System.currentTimeMillis())
                )
            )
        }
        return list
    }

    fun add(context: Context, resource: LocalResource) {
        val current = getAll(context).toMutableList()
        current.add(resource)
        saveAll(context, current)
    }

    fun remove(context: Context, id: String) {
        val current = getAll(context).toMutableList()
        val filtered = current.filterNot { it.id == id }
        saveAll(context, filtered)
    }

    private fun saveAll(context: Context, list: List<LocalResource>) {
        val arr = JSONArray()
        list.forEach { r ->
            val o = JSONObject()
            o.put("id", r.id)
            o.put("title", r.title)
            o.put("path", r.path)
            o.put("mimeType", r.mimeType)
            o.put("timestamp", r.timestamp)
            arr.put(o)
        }
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY, arr.toString()).apply()
    }
}


