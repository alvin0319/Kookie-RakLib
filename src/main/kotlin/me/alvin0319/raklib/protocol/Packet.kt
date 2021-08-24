package me.alvin0319.raklib.protocol

import me.alvin0319.raklib.binary.BinaryDataException
import me.alvin0319.raklib.protocol.types.PacketIdentifier

@PacketIdentifier(-1)
abstract class Packet {

    open fun encode(output: PacketSerializer) {
        encodeHeader(output)
        encodePayload(output)
    }

    protected open fun encodeHeader(output: PacketSerializer) {
        output.putByte(this::class.java.getAnnotation(PacketIdentifier::class.java).id)
    }

    protected abstract fun encodePayload(output: PacketSerializer)

    @Throws(BinaryDataException::class)
    fun decode(input: PacketSerializer) {
        decodeHeader(input)
        decodePayload(input)
    }

    @Throws(BinaryDataException::class)
    fun decodeHeader(input: PacketSerializer) {
        input.getByte() // pass packet id
    }

    @Throws(BinaryDataException::class)
    protected abstract fun decodePayload(input: PacketSerializer)
}
