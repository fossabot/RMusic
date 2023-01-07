package cn.rtast.rmusic.client

import cn.rtast.rmusic.client.commands.RMusicCommand
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback

@Environment(EnvType.CLIENT)
class RMusicClient : ClientModInitializer {
    override fun onInitializeClient() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            RMusicCommand().register(dispatcher)
        }
    }
}