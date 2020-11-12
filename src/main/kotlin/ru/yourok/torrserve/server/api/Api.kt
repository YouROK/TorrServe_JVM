package ru.yourok.torrserve.server.api

import com.google.gson.Gson
import org.jsoup.Connection
import org.jsoup.Jsoup
import ru.yourok.torrserve.models.torrents.Torrent
import ru.yourok.torrserve.settings.Settings

object Api {
    /// Torrents
    fun addTorrent(link: String, title: String, poster: String): Torrent {
        val host = Settings.get()

        val req = """{"action":"get", "link":"$link", "title":"$title","poster":"$poster"}"""

        val resp = Jsoup.connect("$host/torrents")
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .followRedirects(true)
            .ignoreHttpErrors(true)
            .ignoreContentType(true)
            .method(Connection.Method.POST)
            .requestBody(req)
            .execute()

        if (resp.statusCode() != 200)
            throw Exception("error add torrent: ${resp.body()}")

        return Gson().fromJson(resp.body(), Torrent::class.java)
    }

    fun getTorrent() {
    }

    fun remTorrent() {
    }

    fun listTorrent() {
    }

    fun dropTorrent() {
    }

    // Settings
    fun getSettings() {
    }

    fun setSettings() {
    }

    // Viewed
    fun listViewed() {
    }

    fun setViewed() {
    }

    fun remViewed() {
    }
}