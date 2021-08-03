/**
 *
 * _  __           _    _            ____       _    _     _ _
 * | |/ /___   ___ | | _(_) ___      |  _ \ __ _| | _| |   (_) |__
 * | ' // _ \ / _ \| |/ / |/ _ \_____| |_) / _` | |/ / |   | | '_ \
 * | . \ (_) | (_) |   <| |  __/_____|  _ < (_| |   <| |___| | |_) |
 * |_|\_\___/ \___/|_|\_\_|\___|     |_| \_\__,_|_|\_\_____|_|_.__/
 *
 * Copyright (C) 2021 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package me.alvin0319.raklib.server

import io.netty.buffer.ByteBuf
import java.net.InetSocketAddress

interface ServerEventListener {

    fun onClientConnect(sessionId: Int, address: InetSocketAddress, clientId: Int)

    fun onClientDisconnect(sessionId: Int, reason: String)

    fun onPacketReceive(sessionId: Int, packet: ByteBuf)

    fun onRawPacketReceive(address: InetSocketAddress, packet: ByteBuf)

    fun onPacketAck(sessionId: Int, identifierACK: Int)

    fun onBandwidthStatsUpdate(bbytesSentDiff: Int, bytesReceivedDiff: Int)

    fun onpingMeasure(sessionId: Int, pingMS: Int)
}
