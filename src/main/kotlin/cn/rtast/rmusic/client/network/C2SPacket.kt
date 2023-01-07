package cn.rtast.rmusic.client.network

import cn.rtast.rmusic.identifiers.RMusicChannel
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs

class C2SPacket {
    fun send(msg: String) {
        ClientPlayNetworking.send(RMusicChannel.OPERATION_ID, PacketByteBufs.create().writeString(msg))
    }
}