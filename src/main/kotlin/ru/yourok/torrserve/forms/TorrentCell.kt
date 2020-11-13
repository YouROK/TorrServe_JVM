package ru.yourok.torrserve.forms

import javafx.application.Platform
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.util.Callback
import ru.yourok.torrserve.models.torrents.Torrent


class TorrentCell : ListCell<Torrent>() {

    private val title = Label()
    private val hash = Label()
    private val bp = VBox(
        title, hash
    )

    init {
        title.font = Font.font(null, FontWeight.BOLD, 14.0)
    }

    override fun updateItem(torr: Torrent?, empty: Boolean) {
        super.updateItem(torr, empty)
        text = null
        if (empty) {
            graphic = null
        } else {
            val name = if (torr?.title?.isNotEmpty() == true)
                torr.title
            else
                torr?.name

            Platform.runLater {
                title.text = name
                hash.text = torr?.hash ?: ""
                graphic = bp
            }
        }
    }
}


class TorrCellFactory : Callback<ListView<Torrent>?, ListCell<Torrent>?> {
    override fun call(param: ListView<Torrent>?): ListCell<Torrent>? {
        return TorrentCell()
    }
}