package ru.yourok.torrserve.server.api

import com.google.gson.Gson
import org.jsoup.Connection
import org.jsoup.Jsoup
import ru.yourok.torrserve.models.torrents.Torrent
import ru.yourok.torrserve.settings.BTSets
import ru.yourok.torrserve.settings.Settings

object Api {
    class ApiException(msg: String, val code: Int) : Exception(msg)

    /// Echo
    fun echo(): String {
        try {
            val host = Settings.get().host
            val resp = Jsoup.connect("$host/echo")
                .followRedirects(true)
                .method(Connection.Method.GET)
                .execute()
            return resp.body()
        } catch (e: Exception) {
            println(e.message)
            return ""
        }
    }

    /// Torrents
    fun addTorrent(link: String, title: String, poster: String): Torrent {
        val host = Settings.get().host
        val req = TorrentReq("add", link = link, title = title, poster = poster).toString()
        val resp = postJson("$host/torrents", req)
        return Gson().fromJson(resp, Torrent::class.java)
    }

    fun getTorrent(hash: String): Torrent {
        val host = Settings.get().host
        val req = TorrentReq("get", hash).toString()
        val resp = postJson("$host/torrents", req)
        return Gson().fromJson(resp, Torrent::class.java)
    }

    fun remTorrent(hash: String) {
        val host = Settings.get().host
        val req = TorrentReq("rem", hash).toString()
        postJson("$host/torrents", req)
    }

    fun listTorrent(): List<Torrent> {
        val host = Settings.get().host
        val req = TorrentReq("list").toString()
        val resp = postJson("$host/torrents", req)
        return Gson().fromJson(resp, Array<Torrent>::class.java).toList()
    }

    fun dropTorrent(hash: String) {
        val host = Settings.get().host
        val req = TorrentReq("drop", hash).toString()
        postJson("$host/torrents", req)
    }

    // Settings
    fun getSettings(): BTSets {
        val host = Settings.get().host
        val req = Request("get").toString()
        val resp = postJson("$host/settings", req)
        return Gson().fromJson(resp, BTSets::class.java)
    }

    fun setSettings(sets: BTSets) {
        val host = Settings.get().host
        val req = SettingsReq("set", sets).toString()
        postJson("$host/settings", req)
    }

    // Viewed
    fun listViewed(hash: String): List<Viewed> {
        val host = Settings.get().host
        val req = ViewedReq("list", hash).toString()
        val resp = postJson("$host/viewed", req)
        return Gson().fromJson(resp, Array<Viewed>::class.java).toList()
    }

    fun setViewed(hash: String, index: Int) {
        val host = Settings.get().host
        val req = ViewedReq("set", hash, index).toString()
        postJson("$host/viewed", req)
    }

    fun remViewed(hash: String) {
        val host = Settings.get().host
        val req = ViewedReq("rem", hash).toString()
        postJson("$host/viewed", req)
    }

    private fun postJson(url: String, json: String): String {
        val resp = Jsoup.connect(url)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .followRedirects(true)
            .ignoreContentType(true)
            .method(Connection.Method.POST)
            .requestBody(json)
            .execute()

//        if (resp.statusCode() != 200)
//            throw ApiException("error send request: ${resp.body()} ${resp.statusMessage()}", resp.statusCode())
        return resp.body()
    }
}