package cn.rtast.rmusic.common.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.arguments.StringArgumentType.*
import com.mojang.brigadier.arguments.IntegerArgumentType.*
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.command.CommandManager.*

interface IRMusicCommand {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(literal("rmusic").requires { it.hasPermissionLevel(0) }
            .then(literal("play").then(argument("id", integer()).executes {
                playMusic(
                    it,
                    getInteger(it, "id")
                );1
            }))
            .then(literal("stop").executes { stopMusic(it);1 }).then(literal("resume").executes { resumeMusic(it);1 })
            .then(literal("pause").executes { pauseMusic(it);1 }).then(literal("mute").executes { muteMusic(it);1 })
            .then(literal("search").then(argument("keyword", string()).executes {
                searchNetease(
                    it,
                    getString(it, "keyword")
                );1
            })).then(literal("login").requires { it.hasPermissionLevel(4) }
                .then(argument("email", string()).then(argument("password", string()).executes {
                    loginNetease(
                        it, getString(it, "email"), getString(it, "password")
                    );1
                })))
            .then(literal("logout").requires { it.hasPermissionLevel(4) }.executes { logoutNetease(it);1 })
        )
    }

    fun playMusic(ctx: CommandContext<ServerCommandSource>, id: Int)
    fun stopMusic(ctx: CommandContext<ServerCommandSource>)
    fun resumeMusic(ctx: CommandContext<ServerCommandSource>)
    fun pauseMusic(ctx: CommandContext<ServerCommandSource>)
    fun muteMusic(ctx: CommandContext<ServerCommandSource>)
    fun searchNetease(ctx: CommandContext<ServerCommandSource>, keyword: String)
    fun loginNetease(ctx: CommandContext<ServerCommandSource>, email: String, password: String)
    fun logoutNetease(ctx: CommandContext<ServerCommandSource>)
}
