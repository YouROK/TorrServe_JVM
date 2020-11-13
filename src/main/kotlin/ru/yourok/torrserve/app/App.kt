package ru.yourok.torrserve.app

import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import ru.yourok.torrserve.server.service.Service
import java.net.URL


class App : Application() {

    override fun start(stage: Stage) {
        val loader = FXMLLoader()
        val xmlUrl: URL = javaClass.getResource("/forms/main.fxml")
        loader.location = xmlUrl
        val root = loader.load<Parent>()
        stage.title = "TorrServe"
        stage.scene = Scene(root)
        stage.show()
        stage.setOnCloseRequest {
            Service.onTorrentChange = null
            Service.onStatusChange = null
            Service.onListChange = null
            Service.stop()
            Platform.exit();
            System.exit(0);
        }
    }


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(App::class.java)
        }
    }
}