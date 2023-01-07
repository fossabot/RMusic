package cn.rtast.rmusic.client.commands

import cn.rtast.rmusic.client.network.C2SPacket
import cn.rtast.rmusic.common.commands.IRMusicCommand
import cn.rtast.rmusic.music.Music163
import cn.rtast.rmusic.utils.MusicPlayerStatusUtil
import com.google.gson.Gson
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

class RMusicCommand : IRMusicCommand {

    private val gson = Gson()

    private fun send(msg: String) {
        C2SPacket().send(msg)
    }

    override fun playMusic(ctx: CommandContext<ServerCommandSource>, id: Int) {
        if (ctx.source.server.isSingleplayer) {
            Thread {
                MusicPlayerStatusUtil().play(id)
            }.start()
        } else {
            send("play-$id")
        }
    }

    override fun stopMusic(ctx: CommandContext<ServerCommandSource>) {
        if (ctx.source.server.isSingleplayer) {
            MusicPlayerStatusUtil().stop()
        } else {
            send("stop")
        }
    }

    override fun resumeMusic(ctx: CommandContext<ServerCommandSource>) {
        if (ctx.source.server.isSingleplayer) {
            MusicPlayerStatusUtil().resume()
        } else {
            send("resume")
        }
    }

    override fun pauseMusic(ctx: CommandContext<ServerCommandSource>) {
        if (ctx.source.server.isSingleplayer) {
            MusicPlayerStatusUtil().pause()
        } else {
            send("pause")
        }
    }

    override fun muteMusic(ctx: CommandContext<ServerCommandSource>) {
        if (ctx.source.server.isSingleplayer) {
            MusicPlayerStatusUtil().mute()
        } else {
            send("mute")
        }
    }

    override fun searchNetease(ctx: CommandContext<ServerCommandSource>, keyword: String) {
        if (ctx.source.server.isSingleplayer) {
            Thread {
                val result = Music163().cloudsearch(keyword)
                ctx.source.sendFeedback(Text.literal(gson.toJson(result)), false)
            }.start()
        } else {
            send("search-$keyword")
        }
    }

    override fun loginNetease(ctx: CommandContext<ServerCommandSource>, email: String, password: String) {
        if (ctx.source.server.isSingleplayer) {
            Thread {
                Music163().login(email, password)
            }.start()
        } else {
            send("login-$email|$password")
        }
    }

    override fun logoutNetease(ctx: CommandContext<ServerCommandSource>) {
        if (ctx.source.server.isSingleplayer) {
            Music163().logout()
        } else {
            send("logout")
        }
    }
}