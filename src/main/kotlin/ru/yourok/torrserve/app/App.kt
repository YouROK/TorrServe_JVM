package ru.yourok.torrserve.app

import javafx.application.Application
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.Menu
import javafx.stage.Stage
import ru.yourok.torrserve.models.torrents.Torrent
import java.net.URL
import kotlin.concurrent.thread


class App : Application() {

    override fun start(stage: Stage) {
        val loader = FXMLLoader()
        val xmlUrl: URL = javaClass.getResource("/forms/main.fxml")
        loader.location = xmlUrl
        val root = loader.load<Parent>()
        stage.title = "TorrServe"
        stage.scene = Scene(root)
        stage.show()
    }

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
    fun initialize() {
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

    private var updated = false
    private fun update() {
        if (updated)
            return
        updated = true
        thread {
            while (true) {

                Thread.sleep(1000)
            }
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(App::class.java)
        }
    }
}