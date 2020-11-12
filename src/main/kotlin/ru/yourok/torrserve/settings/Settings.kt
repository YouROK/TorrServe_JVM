package ru.yourok.torrserve.settings

object Settings {
    private var settings: Sets = getDef()

    fun get(): Sets {
        return settings
    }

    fun loadSets() {

    }

    fun saveSets() {

    }

    private fun getDef(): Sets {
        return Sets(
            "http://127.0.0.1:8090"
        )
    }
}

data class Sets(
    var host: String
)