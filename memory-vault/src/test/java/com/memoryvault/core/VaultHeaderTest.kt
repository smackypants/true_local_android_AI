package com.memoryvault.core

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class VaultHeaderTest {

    @Test
    fun toBytesAndFromBytes_roundTripNewFormat() {
        val header = VaultHeader(
            version = VaultHeader.CURRENT_VERSION,
            keyVersion = 1,
            indexOffset = 8192L,
            indexSize = 1024L,
            contentOffset = VaultHeader.HEADER_SIZE.toLong(),
            createdTime = 1_000_000L,
            modifiedTime = 2_000_000L
        )

        val decoded = VaultHeader.fromBytes(header.toBytes())

        assertArrayEquals(VaultHeader.MAGIC_BYTES, decoded.magic)
        assertEquals(header.version, decoded.version)
        assertEquals(header.keyVersion, decoded.keyVersion)
        assertEquals(header.indexOffset, decoded.indexOffset)
        assertEquals(header.indexSize, decoded.indexSize)
        assertEquals(header.contentOffset, decoded.contentOffset)
        assertEquals(header.createdTime, decoded.createdTime)
        assertEquals(header.modifiedTime, decoded.modifiedTime)
    }

    @Test
    fun toBytesAndFromBytes_defaultHeaderIsValid() {
        val header = VaultHeader()

        val decoded = VaultHeader.fromBytes(header.toBytes())

        assertTrue(decoded.isValid())
        assertEquals(VaultHeader.CURRENT_VERSION, decoded.version)
        assertEquals(VaultHeader.HEADER_SIZE.toLong(), decoded.contentOffset)
    }

    @Test
    fun isValid_returnsTrueForCorrectMagicBytes() {
        val header = VaultHeader()

        assertTrue(header.isValid())
    }

    @Test
    fun isValid_returnsFalseForCorruptedMagicBytes() {
        val header = VaultHeader(magic = byteArrayOf(0, 0, 0, 0))

        assertFalse(header.isValid())
    }

    @Test
    fun fromBytes_parsesOldFormatHeaderWithoutKeyVersionField() {
        // Old format: after version (2 bytes), indexOffset starts immediately,
        // and contentOffset will not equal HEADER_SIZE.
        val header = VaultHeader(
            version = VaultHeader.CURRENT_VERSION,
            keyVersion = 0,
            indexOffset = 0L,
            indexSize = 0L,
            contentOffset = 512L, // != HEADER_SIZE, triggers old-format path
            createdTime = 100L,
            modifiedTime = 200L
        )
        // Build old-format bytes manually (no keyVersion field)
        val oldBytes = ByteArray(VaultHeader.HEADER_SIZE)
        val magic = VaultHeader.MAGIC_BYTES
        magic.copyInto(oldBytes, 0)
        // version at [4..5]
        oldBytes[4] = (VaultHeader.CURRENT_VERSION.toInt() and 0xFF).toByte()
        oldBytes[5] = ((VaultHeader.CURRENT_VERSION.toInt() shr 8) and 0xFF).toByte()
        // indexOffset at [6..13]
        writeLongLE(oldBytes, 6, 0L)
        // indexSize at [14..21]
        writeLongLE(oldBytes, 14, 0L)
        // contentOffset at [22..29]  – NOT 256, so looks like old format
        writeLongLE(oldBytes, 22, 512L)
        // createdTime at [30..37]
        writeLongLE(oldBytes, 30, 100L)
        // modifiedTime at [38..45]
        writeLongLE(oldBytes, 38, 200L)

        val decoded = VaultHeader.fromBytes(oldBytes)

        assertTrue(decoded.isValid())
        // Old format defaults keyVersion to 0
        assertEquals(0.toShort(), decoded.keyVersion)
        assertEquals(0L, decoded.indexOffset)
        assertEquals(0L, decoded.indexSize)
        assertEquals(100L, decoded.createdTime)
        assertEquals(200L, decoded.modifiedTime)
    }

    @Test(expected = IllegalArgumentException::class)
    fun fromBytes_throwsForTooSmallByteArray() {
        VaultHeader.fromBytes(ByteArray(VaultHeader.HEADER_SIZE - 1))
    }

    @Test
    fun toBytes_producesCorrectMagicAtStart() {
        val bytes = VaultHeader().toBytes()

        assertArrayEquals(VaultHeader.MAGIC_BYTES, bytes.copyOf(4))
    }

    @Test
    fun toBytes_producesExactHeaderSizeBytes() {
        val bytes = VaultHeader().toBytes()

        assertEquals(VaultHeader.HEADER_SIZE, bytes.size)
    }

    // Helper: write Long little-endian into a byte array at given offset
    private fun writeLongLE(buf: ByteArray, offset: Int, value: Long) {
        for (i in 0..7) {
            buf[offset + i] = ((value ushr (i * 8)) and 0xFF).toByte()
        }
    }
}
