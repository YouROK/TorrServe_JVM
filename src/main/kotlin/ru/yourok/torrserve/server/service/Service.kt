package ru.yourok.torrserve.server.service

import javafx.application.Platform
import ru.yourok.torrserve.models.torrents.Torrent
import ru.yourok.torrserve.server.api.Api
import kotlin.concurrent.thread

object Service {
    var onStatusChange: ((status: String) -> Unit)? = null
    var onListChange: ((list: List<Torrent>) -> Unit)? = null
    var onTorrentChange: ((torr: Torrent?) -> Unit)? = null

    var selectedTorrent: Torrent? = null

    private var lastStatus = ""
    private var lastList = emptyList<Torrent>()
    private var lastTorrent: Torrent? = null

    private var stop = false

    fun start() {
        stop = false
        update()
    }

    fun stop() {
        stop = true
    }

    private var updated = false
    private fun update() {
        if (updated)
            return
        updated = true
        thread {
            while (true) {
                val ver = Api.echo()

                val status = if (ver.isNullOrEmpty())
                    "Error connect ot server"
                else
                    "Connect ot server: $ver"

                if (lastStatus != status) {
                    lastStatus = status
                    Platform.runLater {
                        onStatusChange?.invoke(status)
                    }
                }

                var list = emptyList<Torrent>()
                try {
                    list = Api.listTorrent()
                } catch (e: Exception) {
                }
                if (list != lastList) {
                    lastList = list
                    Platform.runLater {
                        onListChange?.invoke(list)
                    }
                }

                selectedTorrent?.let { torr ->
                    try {
                        selectedTorrent = Api.getTorrent(torr.hash)
                        if (selectedTorrent != lastTorrent) {
                            Platform.runLater {
                                onTorrentChange?.invoke(selectedTorrent)
                            }
                        }
                    } catch (e: Exception) {
                    }
                }

                Thread.sleep(1000)
            }
        }
    }

}