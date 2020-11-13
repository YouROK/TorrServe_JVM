package ru.yourok.torrserve.forms

import javafx.application.Platform
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.Menu
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import ru.yourok.torrserve.models.torrents.FileStat
import ru.yourok.torrserve.models.torrents.Torrent
import ru.yourok.torrserve.server.service.Service
import ru.yourok.torrserve.utils.Utils
import ru.yourok.torrserve.utils.Utils.preloadTorrent
import kotlin.concurrent.thread


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
    var lbStat: Label? = null

    @FXML
    var lbHash: Label? = null

    @FXML
    var lbName: Label? = null

    @FXML
    var lbSize: Label? = null

    @FXML
    var lbSpeed: Label? = null

    @FXML
    var lbPreload: Label? = null

    @FXML
    var lbActivePeers: Label? = null

    @FXML
    var lbConnPeers: Label? = null

    @FXML
    var lbAllPeers: Label? = null

    @FXML
    var btnOpenLink: Button? = null

    @FXML
    var btnCopyLink: Button? = null

    @FXML
    var lvFiles: ListView<FileStat>? = null

    @FXML
    var btnPreload: Button? = null

    private val torrentObservableList: ObservableList<Torrent> = javafx.collections.FXCollections.observableArrayList()
    private val fileObservableList: ObservableList<FileStat> = javafx.collections.FXCollections.observableArrayList()
    private var selectedTorrent: Torrent? = null
    private var selectedFile: FileStat? = null

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
        cleanInfo()
        Service.start()

        lvTorrents?.items = torrentObservableList
        lvTorrents?.cellFactory = TorrCellFactory()

        lvTorrents?.selectionModel?.selectedItemProperty()?.addListener { _, _, newValue ->
            Service.selectedTorrent = newValue
            updateInfo(newValue)
        }

        lvFiles?.items = fileObservableList
        lvFiles?.cellFactory = FileCellFactory()
        lvFiles?.selectionModel?.selectedItemProperty()?.addListener { _, _, newValue ->
            selectedFile = newValue
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

        lbHash?.setOnMouseClicked {
            if (it.clickCount >= 2) {
                val clipboard: Clipboard = Clipboard.getSystemClipboard()
                val content = ClipboardContent()
                content.putString(lbHash?.text ?: return@setOnMouseClicked)
                clipboard.setContent(content)
            }
        }

        btnOpenLink?.setOnAction {
//            HostServices().showDocument()
        }

        btnCopyLink?.setOnAction {

        }

        btnPreload?.setOnAction {
            selectedTorrent?.let {
                if (it.stat != 2)
                    thread {
                        preloadTorrent(it)
                    }
            }
        }
    }

    fun onSelected(): (torr: Torrent?) -> Unit {
        return { torr ->
            updateInfo(torr)
        }
    }

    private fun updateFiles(torr: Torrent?) {
        if (torr?.file_stats?.size ?: 0 != fileObservableList.size || torr != selectedTorrent) {
            fileObservableList.clear()
            torr?.file_stats?.let {
                fileObservableList.addAll(it)
            }
        }
    }

    private fun updateInfo(torr: Torrent?) {
        if (torr == null)
            cleanInfo()
        else {
            if (selectedTorrent?.poster != torr.poster && torr.poster.isNotEmpty())
                Platform.runLater {
                    ivPoster?.image = Image(torr.poster)
                }
            if (torr.poster.isEmpty())
                ivPoster?.image = Image("/img/ep.png")

            lbTitle?.text = torr.title
            lbStat?.text = torr.stat_string
            lbHash?.text = torr.hash
            lbName?.text = torr.name
            lbSize?.text = Utils.formatSize(torr.torrent_size.toDouble())
            if (torr.preloaded_bytes < torr.preload_size) {
                lbPreload?.text = Utils.formatSize(torr.preloaded_bytes.toDouble()) + "/" +
                        Utils.formatSize(torr.preload_size.toDouble()) + " " +
                        (torr.preloaded_bytes * 100 / torr.preload_size).toString() + "%"
            }
            lbSpeed?.text = Utils.formatSize(torr.download_speed) + "/sec"
            lbActivePeers?.text = torr.active_peers.toString()
            lbConnPeers?.text = torr.connected_seeders.toString()
            lbAllPeers?.text = torr.total_peers.toString()

            updateFiles(torr)

            selectedTorrent = torr
        }
    }

    private fun cleanInfo() {
        ivPoster?.image = null
        lbTitle?.text = ""
        lbStat?.text = ""
        lbHash?.text = ""
        lbName?.text = ""
        lbSize?.text = ""
        lbSpeed?.text = ""
        lbPreload?.text = ""
        lbActivePeers?.text = ""
        lbConnPeers?.text = ""
        lbAllPeers?.text = ""
    }


}