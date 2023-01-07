package cn.rtast.rmusic.utils

import cn.rtast.rmusic.data.netease.player.PlayerStatus
import cn.rtast.rmusic.music.Music163
import cn.rtast.rmusic.player.CommonPlayer
import com.google.gson.Gson
import java.io.File
import java.io.FileWriter

class MusicPlayerStatusUtil {
    init {
        val file = File("./rmusic/playing.json")
        if (!file.exists()) {
            file.createNewFile()
            file.writeText("{\"status\": \"stopped\", \"url\": \"null\", \"name\": \"null\"}")
        }
    }

    private val gson = Gson()
    private val playing = File("./rmusic/playing.json")
    val playerStatus: PlayerStatus = gson.fromJson(playing.readText(), PlayerStatus::class.java)


    private fun save() {
        val fileWriter = FileWriter(playing.name)
        fileWriter.write(gson.toJson(playerStatus).toString())
    }

    fun play(id: Int): Int {
        if (playerStatus.status == "playing") {
            playerStatus.status = "new"
            Thread {
                val data = Music163().getSongUrl(id)
                playerStatus.url = data.url
                CommonPlayer().playMusic(data.url, data.br.toFloat())
            }.start()
            save()
            return -1
        }
        Thread {
            val data = Music163().getSongUrl(id)
            playerStatus.url = data.url
            CommonPlayer().playMusic(data.url, data.br.toFloat())
        }.start()
        save()
        return 0
    }

    fun pause(): Int {
        if (playerStatus.status != "stopped") {
            playerStatus.status = "paused"
        }
        save()
        return -1
    }

    fun resume(): Int {
        if (playerStatus.status != "stopped" && playerStatus.status == "paused") {
            playerStatus.status = "playing"
            return 0
        }
        save()
        return -1
    }

    fun stop(): Int {
        if (playerStatus.status != "stopped") {
            playerStatus.status = "stopped"
            return 0
        }
        save()
        return -1
    }

    fun mute(): Int {
        return if (playerStatus.status != "muted") {
            playerStatus.status = "muted"
            save()
            0
        } else {
            playerStatus.status = "playing"
            save()
            -1
        }
    }

    fun isPlaying(): Boolean {
        return playerStatus.status == "playing" || playerStatus.status == "new"
    }

    fun isMuted(): Boolean {
        return playerStatus.status == "muted"
    }

    fun isStopped(): Boolean {
        return playerStatus.status == "stopped"
    }

    fun isPaused(): Boolean {
        return playerStatus.status == "paused"
    }
}