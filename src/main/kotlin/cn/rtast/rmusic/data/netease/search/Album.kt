package cn.rtast.rmusic.data.netease.search

data class Album(
    val alia: List<String>,
    val artist: ArtistX,
    val copyrightId: Int,
    val id: Int,
    val mark: Int,
    val name: String,
    val picId: Long,
    val publishTime: Long,
    val size: Int,
    val status: Int
)