package cn.rtast.rmusic.client.events

import net.minecraft.network.PacketByteBuf

class OnOPMessage {
    fun onMessage(buf: PacketByteBuf) {
        println(buf.readString())
    }
}