package me.alvin0319.raklib.protocol

import com.koloboke.collect.map.hash.HashIntIntMaps
import me.alvin0319.raklib.binary.Binary
import me.alvin0319.raklib.binary.BinaryDataException

abstract class AcknowledgePacket : Packet() {

    val packets: MutableMap<Int, Int> = HashIntIntMaps.newMutableMap()

    override fun encodePayload(output: PacketSerializer) {
        val payload = StringBuilder("")

        val sorted = packets.toSortedMap()

        val count = sorted.size
        var records = 0

        if (count > 0) {
            var pointer = 1
            var start = sorted[0] ?: throw BinaryDataException("Invalid AcknowledgePacket received")
            var last = sorted[0] ?: throw BinaryDataException("Invalid AcknowledgePacket received")

            while (pointer < count) {
                val current = sorted[pointer++]
                val diff = current?.minus(last)
                if (diff == 1) {
                    last = current
                } else if (diff != null) {
                    if (diff > 1) {
                        if (start == last) {
                            payload.append(RECORD_TYPE_SINGLE.toChar())
                            payload.append(Binary.writeLTriad(start))
                            start = current
                            last = current
                        } else {
                            payload.append(RECORD_TYPE_RANGE)
                            payload.append(Binary.writeLTriad(start))
                            payload.append(Binary.writeLTriad(last))
                            start = current
                            last = current
                        }
                        ++records
                    }
                }
            }
            if (start == last) {
                payload.append(RECORD_TYPE_SINGLE.toChar())
                payload.append(Binary.writeLTriad(start))
            } else {
                payload.append(RECORD_TYPE_RANGE.toChar())
                payload.append(Binary.writeLTriad(start))
                payload.append(Binary.writeLTriad(last))
            }
            ++records
        }
        output.putShort(records)
        output.put(payload.toString())
    }

    override fun decodePayload(input: PacketSerializer) {
        val count = input.getShort()
        var cnt = 0

        for (i in 0 until count) {
            if (input.feof() || cnt >= 4096) {
                break
            }
            if (input.getByte() == RECORD_TYPE_RANGE) {
                val start = input.getLTriad()
                var end = input.getLTriad()
                if ((end - start) > 512) {
                    end = start + 512
                }
                for (c in 0..end) {
                    packets[cnt++] = c
                }
            } else {
                packets[cnt++] = input.getLTriad()
            }
        }
    }

    companion object {
        const val RECORD_TYPE_RANGE = 0
        const val RECORD_TYPE_SINGLE = 1
    }
}
