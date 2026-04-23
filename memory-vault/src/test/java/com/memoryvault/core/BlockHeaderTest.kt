package com.memoryvault.core

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.UUID

class BlockHeaderTest {

    @Test
    fun toBytesAndFromBytes_roundTripPreservesFields() {
        val header = BlockHeader(
            blockId = UUID.fromString("11111111-2222-3333-4444-555555555555"),
            blockType = BlockType.MESSAGE,
            contentSize = 1024L,
            timestamp = 123456789L,
            checksum = 987654321,
            compressionFlag = true,
            encryptionFlag = true
        )

        val decoded = BlockHeader.fromBytes(header.toBytes())

        assertEquals(header, decoded)
    }

    @Test(expected = IllegalArgumentException::class)
    fun fromBytes_throwsForTooSmallByteArray() {
        BlockHeader.fromBytes(ByteArray(BlockHeader.HEADER_SIZE - 1))
    }

    @Test
    fun calculateChecksum_isDeterministic() {
        val checksum = BlockHeader.calculateChecksum("abc".toByteArray())

        assertEquals(891568578, checksum)
    }
}

