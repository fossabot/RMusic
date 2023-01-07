package cn.rtast.rmusic.commands

import cn.rtast.rmusic.common.commands.IRMusicCommand
import cn.rtast.rmusic.music.Music163
import cn.rtast.rmusic.network.S2CPacket
import com.google.gson.Gson
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource

class RMusicCommand : IRMusicCommand {

    private fun send(msg: String, ctx: CommandContext<ServerCommandSource>) {
        S2CPacket().send(msg, ctx.source.player!!)
    }

    override fun playMusic(ctx: CommandContext<ServerCommandSource>, id: Int) {
        Thread {
            val result = Music163().getSongUrl(id)
            send("play-${result.url}|${result.br}", ctx)
        }.start()
    }

    override fun stopMusic(ctx: CommandContext<ServerCommandSource>) {
        send("stop", ctx)
    }

    override fun resumeMusic(ctx: CommandContext<ServerCommandSource>) {
        send("resume", ctx)
    }

    override fun pauseMusic(ctx: CommandContext<ServerCommandSource>) {
        send("pause", ctx)
    }

    override fun muteMusic(ctx: CommandContext<ServerCommandSource>) {
        send("mute", ctx)
    }

    override fun searchNetease(ctx: CommandContext<ServerCommandSource>, keyword: String) {
        Thread {
            var result = ""
            val data = Music163().cloudsearch(keyword)
            data.forEach {
                result += it.name
                it.artists.forEach { ar ->
                    result += ar.name
                }
                result += "${it.id}\n"
            }
            send(result, ctx)
        }.start()
    }

    override fun loginNetease(ctx: CommandContext<ServerCommandSource>, email: String, password: String) {
        send("login-$email|$password", ctx)
    }

    override fun logoutNetease(ctx: CommandContext<ServerCommandSource>) {
        send("logout", ctx)
    }
}