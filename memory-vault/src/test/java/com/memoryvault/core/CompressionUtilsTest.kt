package com.memoryvault.core

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Random

class CompressionUtilsTest {

    @Test
    fun shouldCompress_returnsFalseForSmallPayload() {
        val data = ByteArray(128) { 1 }

        assertFalse(CompressionUtils.shouldCompress(data))
    }

    @Test
    fun shouldCompress_returnsTrueForHighlyRepetitiveLargePayload() {
        val data = ByteArray(2048) { 'a'.code.toByte() }

        assertTrue(CompressionUtils.shouldCompress(data))
    }

    @Test
    fun compressAndDecompress_roundTripOriginalData() {
        val random = Random(42)
        val original = ByteArray(4096).also { random.nextBytes(it) }

        val compressed = CompressionUtils.compress(original)
        val decompressed = CompressionUtils.decompress(compressed, original.size)

        assertArrayEquals(original, decompressed)
    }
}

