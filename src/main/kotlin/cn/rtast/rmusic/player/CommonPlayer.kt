package cn.rtast.rmusic.player

import cn.rtast.rmusic.music.Music163
import cn.rtast.rmusic.utils.MusicConverterUtil
import cn.rtast.rmusic.utils.MusicPlayerStatusUtil
import cn.rtast.rmusic.utils.RandomNameUtil
import com.goxr3plus.streamplayer.enums.Status
import com.goxr3plus.streamplayer.stream.StreamPlayer
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent
import com.goxr3plus.streamplayer.stream.StreamPlayerListener
import java.io.File

class CommonPlayer : StreamPlayer(), StreamPlayerListener {

    private val status = MusicPlayerStatusUtil()

    private fun convert(url: String, br: Float) {
        Thread {
            val name = MusicConverterUtil().convert(url, RandomNameUtil().gen(20), br)
            open(File("./rmusic/cache/$name"))
            play()
        }.start()
    }

    fun playMusic(id: Int) {
        val info = Music163().getSongUrl(id)
        convert(info.url, info.br.toFloat())
    }

    fun playMusic(url: String, br: Float) {
        convert(url, br)
    }

    override fun opened(dataSource: Any?, properties: MutableMap<String, Any>?) {
        println("Stream opened...")
    }

    override fun progress(
        nEncodedBytes: Int,
        microsecondPosition: Long,
        pcmData: ByteArray?,
        properties: MutableMap<String, Any>?
    ) {
        if (status.isMuted()) {
            this.mute = !this.mute
        } else if (status.isPaused()) {
            pause()
        } else if (!status.isPlaying()) {
            stop()
        } else if (status.playerStatus.status == "new") {
            stop()
            open(File("./rmusic/cache/${status.playerStatus.name}"))
            play()
        }
    }

    override fun statusUpdated(event: StreamPlayerEvent?) {
        when (event?.playerStatus) {
            Status.STOPPED -> status.stop()
            Status.PAUSED -> status.pause()
            else -> status.resume()
        }
    }
}