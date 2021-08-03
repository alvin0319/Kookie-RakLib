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

import com.koloboke.collect.map.hash.HashIntObjMaps
import com.koloboke.collect.map.hash.HashObjLongMaps
import io.netty.bootstrap.Bootstrap
import io.netty.buffer.ByteBuf
import io.netty.buffer.PooledByteBufAllocator
import io.netty.channel.Channel
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelOption
import io.netty.channel.epoll.Epoll
import io.netty.channel.epoll.EpollDatagramChannel
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioDatagramChannel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress

class Server(
    val serverId: Int,
    val address: InetSocketAddress,
    val maxMtuSize: Int,
    val protocolAcceptor: ProtocolAcceptor,
    val eventSource: ServerEventSource,
    val eventListener: ServerEventListener
) : ServerInterface, ChannelHandler {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    val bootstrap: Bootstrap

    val isAvailableEpoll: Boolean = Epoll.isAvailable()

    val startTimeMs: Long = System.currentTimeMillis()

    var nextSessionId: Long = 0

    var receiveBytes: Int = 0
    var sendBytes: Int = 0

    val sessions: MutableMap<Int, Session> = HashIntObjMaps.newMutableMap()

    val rawPacketFilters: List<Regex> = mutableListOf()

    var ticks: Long = 0

    var packetLimit: Int = 200

    var shutdown: Boolean = false

    val block: MutableMap<InetSocketAddress, Long> = HashObjLongMaps.newMutableMap()

    val ipSec: MutableMap<InetSocketAddress, Long> = HashObjLongMaps.newMutableMap()

    var portChecking: Boolean = false

    val rakNetTimeMS: Long = System.currentTimeMillis() - startTimeMs

    val port: Int = address.port

    init {
        try {
            if (!isAvailableEpoll) {
                logger.debug("Not on windows environment, utilizing epoll...")
            }
            bootstrap = Bootstrap().channel(
                (
                    if (isAvailableEpoll) {
                        EpollDatagramChannel::class.java
                    } else {
                        NioDatagramChannel::class.java
                    }
                    ) as Class<out Channel>?
            )
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(this)
                .group(
                    if (isAvailableEpoll) {
                        EpollEventLoopGroup()
                    } else {
                        NioEventLoopGroup()
                    }
                )
        } catch (e: Throwable) {
            throw RuntimeException("Failed to bind port: ")
        }
    }

    fun tickProcessor() {
        val start = System.currentTimeMillis()
        do {
            var stream = !shutdown
            if (stream && !shutdown) {
                for (i in 0 until 100) {
                    stream = eventSource.process(this)
                }
            }
            var socket = true
            if (socket) {
                for (i in 0 until 100) {
                    socket = receivePacket()
                }
            }
        } while (stream || socket)
        tick()

        val time = System.currentTimeMillis() - start

        if (time < RAKLIB_TIME_PER_TICK) {
            Thread.sleep(System.currentTimeMillis() + RAKLIB_TIME_PER_TICK - time)
        }
    }

    fun waitShutdown() {
        shutdown = true
        // sessions.forEach
    }

    private fun receivePacket(): Boolean {
        TODO("Not yet implemented")
    }

    private fun tick() {
    }

    override fun sendEncapsulated(sessionId: Int, packet: Any, immediate: Boolean) {
        TODO("Not yet implemented")
    }

    override fun sendRaw(address: InetSocketAddress, payload: ByteBuf) {
        TODO("Not yet implemented")
    }

    override fun closeSession(sessionId: Int) {
        TODO("Not yet implemented")
    }

    override fun setName(name: String) {
        TODO("Not yet implemented")
    }

    override fun setPortCheck(value: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setPacketPerTickLimit(limit: Int) {
        TODO("Not yet implemented")
    }

    override fun blockAddress(address: InetSocketAddress, timeout: Long) {
        TODO("Not yet implemented")
    }

    override fun unblockAddress(address: InetSocketAddress) {
        TODO("Not yet implemented")
    }

    override fun addRawPacketFilter(regex: Regex) {
        TODO("Not yet implemented")
    }

    override fun handlerAdded(ctx: ChannelHandlerContext?) {
        TODO("Not yet implemented")
    }

    override fun handlerRemoved(ctx: ChannelHandlerContext?) {
        TODO("Not yet implemented")
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        TODO("Not yet implemented")
    }

    companion object {
        private const val RAKLIB_TPS = 100
        private const val RAKLIB_TIME_PER_TICK = 1 / RAKLIB_TPS
    }
}
