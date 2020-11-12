package ru.yourok.torrserve.models.torrents

data class Torrent(
    var title: String,
    var poster: String,
    var timestamp: Int,
    var name: String,
    var hash: String,
    var stat: Int,
    var stat_string: String,
    var loaded_size: Int,
    var torrent_size: Int,
    var download_speed: Double,
    var total_peers: Int,
    var pending_peers: Int,
    var active_peers: Int,
    var connected_seeders: Int,
    var half_open_peers: Int,
    var bytes_written: Int,
    var bytes_read: Int,
    var bytes_read_data: Int,
    var bytes_read_useful_data: Int,
    var chunks_read: Int,
    var chunks_read_useful: Int,
    var chunks_read_wasted: Int,
    var pieces_dirtied_good: Int,
    var file_stats: List<FileStat>
)

data class FileStat(
    var id: Int,
    var path: String,
    var length: Int
)