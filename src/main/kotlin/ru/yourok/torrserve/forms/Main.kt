package ru.yourok.torrserve.forms

import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.Menu
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import ru.yourok.torrserve.models.torrents.Torrent
import ru.yourok.torrserve.server.service.Service


class Main {
    @FXML
    var lvTorrents: ListView<Torrent>? = null

    @FXML
    var mSettings: Menu? = null

    @FXML
    var mCache: Menu? = null

    @FXML
    var mAbout: Menu? = null

    @FXML
    var lbStatus: Label? = null

    @FXML
    var ivPoster: ImageView? = null

    @FXML
    var lbTitle: Label? = null

    @FXML
    var lbHash: Label? = null

    @FXML
    var lbName: Label? = null

    @FXML
    var lbSize: Label? = null

    @FXML
    var lbSpeed: Label? = null

    @FXML
    var lbActivePeers: Label? = null

    @FXML
    var lbConnPeers: Label? = null

    @FXML
    var lbAllPeers: Label? = null

    private val torrentObservableList: ObservableList<Torrent> = javafx.collections.FXCollections.observableArrayList()

    @FXML
    fun initialize() {
        Service.onStatusChange = {
            lbStatus?.text = it
        }

        Service.onListChange = { list ->
            if (torrentObservableList.size != list.size) {
                torrentObservableList.clear()
                torrentObservableList.addAll(list)
            } else {
                for (i in list.indices) {
                    if (torrentObservableList[i].hash != list[i].hash)
                        torrentObservableList[i] = list[i]
                }
            }
        }

        Service.onTorrentChange = onSelected()
        Service.start()

        lvTorrents?.items = torrentObservableList
        lvTorrents?.cellFactory = TorrCellFactory()

        lvTorrents?.selectionModel?.selectedItemProperty()?.addListener { observable, oldValue, newValue ->
            selTorr = newValue
            Service.selectedTorrent = newValue
        }

        mSettings?.setOnAction {
            //TODO show settings
        }

        mCache?.setOnAction {
            //TODO show cache
        }

        mAbout?.setOnAction {
            //TODO show about
        }
    }

    private var selTorr: Torrent? = null
    fun onSelected(): (torr: Torrent?) -> Unit {
        return { torr ->
            if (selTorr == null)
                cleanInfo()
            else {
                if (selTorr?.poster != torr?.poster && torr?.poster?.isNotEmpty() == true)
                    ivPoster?.imageProperty()?.set(Image(torr.poster))
                if (selTorr?.title != torr?.title)
                    lbTitle?.text = torr?.title ?: ""
                if (selTorr?.hash != torr?.hash)
                    lbHash?.text = torr?.hash ?: ""
                if (selTorr?.name != torr?.name)
                    lbName?.text = torr?.name ?: ""
                if (selTorr?.torrent_size != torr?.torrent_size)
                    lbSize?.text = formatSize(torr?.torrent_size?.toDouble() ?: 0.0)
                if (selTorr?.download_speed != torr?.download_speed)
                    lbSpeed?.text = formatSize(torr?.download_speed ?: 0.0) + "/sec"
                if (selTorr?.active_peers != torr?.active_peers)
                    lbActivePeers?.text = torr?.active_peers?.toString() ?: ""
                if (selTorr?.connected_seeders != torr?.connected_seeders)
                    lbConnPeers?.text = torr?.connected_seeders?.toString() ?: ""
                if (selTorr?.total_peers != torr?.total_peers)
                    lbAllPeers?.text = torr?.total_peers?.toString() ?: ""
            }
        }
    }

    private fun cleanInfo() {
        ivPoster?.imageProperty()?.set(null)
        lbTitle?.text = ""
        lbHash?.text = ""
        lbName?.text = ""
        lbSize?.text = ""
        lbSpeed?.text = ""
        lbActivePeers?.text = ""
        lbConnPeers?.text = ""
        lbAllPeers?.text = ""
    }

    private fun formatSize(v: Double): String? {
        if (v < 1024.0) return "$v B"
        val z = (63 - java.lang.Long.numberOfLeadingZeros(v.toLong())) / 10
        return String.format("%.1f %sB", v / (1L shl z * 10), " KMGTPE"[z])
    }
}