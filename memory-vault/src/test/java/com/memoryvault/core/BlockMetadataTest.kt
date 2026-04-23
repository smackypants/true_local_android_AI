package com.memoryvault.core

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.UUID

class BlockMetadataTest {

    @Test
    fun toBytesAndFromBytes_roundTripWithPopulatedFields() {
        val metadata = BlockMetadata(
            blockId = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee"),
            blockType = BlockType.FILE,
            fileOffset = 4096L,
            compressedSize = 512L,
            uncompressedSize = 2048L,
            timestamp = 12345L,
            category = "docs",
            tags = linkedSetOf("tag1", "tag2"),
            contentHash = BlockMetadata.calculateContentHash("hello".toByteArray()),
            searchableText = "some searchable content"
        )

        val decoded = BlockMetadata.fromBytes(metadata.toBytes())

        assertEquals(metadata.blockId, decoded.blockId)
        assertEquals(metadata.blockType, decoded.blockType)
        assertEquals(metadata.fileOffset, decoded.fileOffset)
        assertEquals(metadata.compressedSize, decoded.compressedSize)
        assertEquals(metadata.uncompressedSize, decoded.uncompressedSize)
        assertEquals(metadata.timestamp, decoded.timestamp)
        assertEquals(metadata.category, decoded.category)
        assertEquals(metadata.tags, decoded.tags)
        assertEquals(metadata.contentHash, decoded.contentHash)
        assertEquals(metadata.searchableText, decoded.searchableText)
    }

    @Test
    fun toBytesAndFromBytes_roundTripWithNullAndEmptyOptionalFields() {
        val metadata = BlockMetadata(
            blockId = UUID.fromString("01234567-89ab-cdef-0123-456789abcdef"),
            blockType = BlockType.CUSTOM_DATA,
            fileOffset = 10L,
            compressedSize = 20L,
            uncompressedSize = 30L,
            timestamp = 40L,
            category = null,
            tags = emptySet(),
            contentHash = BlockMetadata.calculateContentHash("payload".toByteArray()),
            searchableText = null
        )

        val decoded = BlockMetadata.fromBytes(metadata.toBytes())

        assertEquals(null, decoded.category)
        assertEquals(emptySet<String>(), decoded.tags)
        assertEquals(null, decoded.searchableText)
    }

    @Test(expected = IllegalArgumentException::class)
    fun fromBytes_throwsForInvalidMetadataSize() {
        BlockMetadata.fromBytes(ByteArray(BlockMetadata.METADATA_SIZE - 1))
    }

    @Test
    fun calculateContentHash_matchesKnownSha256() {
        val hash = BlockMetadata.calculateContentHash("abc".toByteArray())

        assertEquals(
            "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
            hash
        )
    }
}

