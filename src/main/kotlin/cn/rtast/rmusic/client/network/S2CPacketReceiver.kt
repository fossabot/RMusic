package cn.rtast.rmusic.client.network

import cn.rtast.rmusic.client.events.OnOPMessage
import cn.rtast.rmusic.identifiers.RMusicChannel
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.PacketByteBuf

class S2CPacketReceiver {
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(RMusicChannel.OPERATION_ID)
        { _: MinecraftClient,
          _: ClientPlayNetworkHandler,
          buf: PacketByteBuf,
          _: PacketSender ->
            OnOPMessage().onMessage(buf)
        }
    }
}