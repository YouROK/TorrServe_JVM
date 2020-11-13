package ru.yourok.torrserve.utils

import org.jsoup.Jsoup
import ru.yourok.torrserve.models.torrents.Torrent
import ru.yourok.torrserve.settings.Settings

object Utils {
    fun formatSize(v: Double): String? {
        if (v < 1024.0) return "$v B"
        val z = (63 - java.lang.Long.numberOfLeadingZeros(v.toLong())) / 10
        return String.format("%.1f %sB", v / (1L shl z * 10), " KMGTPE"[z])
    }

    fun getPlayLink(torr: Torrent): String {
        // http://127.0.0.1:8090/stream/fname?link=...&index=1&play
//        val host = Settings.get().host + "/stream/" + URLEncoder.encode(torr.
//
//                torr.hash
        return ""
    }

    fun preloadTorrent(torr: Torrent) {
        try {
            val host = Settings.get().host + "/stream/?link=${torr.hash}&preload"
            Jsoup.connect(host).execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}