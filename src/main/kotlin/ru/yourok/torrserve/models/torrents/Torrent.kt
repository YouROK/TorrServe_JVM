package ru.yourok.torrserve.models.torrents

data class Torrent(
        var hash: String,
        var title: String,
        var poster: String,
        var name: String,
)