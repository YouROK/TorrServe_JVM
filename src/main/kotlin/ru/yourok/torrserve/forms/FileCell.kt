package ru.yourok.torrserve.forms

import javafx.application.Platform
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.layout.HBox
import javafx.util.Callback
import ru.yourok.torrserve.models.torrents.FileStat
import ru.yourok.torrserve.utils.Utils

class FileCell : ListCell<FileStat>() {

    private val name = Label()
    private val size = Label()
    private val bp = HBox(
        name, size
    )

    override fun updateItem(file: FileStat?, empty: Boolean) {
        super.updateItem(file, empty)
        text = null
        if (empty) {
            graphic = null
        } else {
            Platform.runLater {
                name.text = file?.path ?: ""
                file?.let { f ->
                    size.text = Utils.formatSize(f.length.toDouble())
                } ?: let {
                    size.text = ""
                }
                graphic = bp
            }
        }
    }
}


class FileCellFactory : Callback<ListView<FileStat>?, ListCell<FileStat>?> {
    override fun call(param: ListView<FileStat>?): ListCell<FileStat>? {
        return FileCell()
    }
}