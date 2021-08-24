package me.alvin0319.raklib.protocol

import me.alvin0319.raklib.protocol.types.PacketIdentifier

@PacketIdentifier(MessageIdentifiers.ID_ADVERTISE_SYSTEM)
class AdvertiseSystem : Packet() {

    lateinit var serverName: String

    override fun encodePayload(output: PacketSerializer) {
        output.putString(serverName)
    }

    override fun decodePayload(input: PacketSerializer) {
        serverName = input.getString()
    }
}
