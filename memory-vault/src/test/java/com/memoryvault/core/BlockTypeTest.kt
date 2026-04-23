package com.memoryvault.core

import org.junit.Assert.assertEquals
import org.junit.Test

class BlockTypeTest {

    @Test
    fun fromCode_returnsExpectedEnumForKnownCodes() {
        assertEquals(BlockType.MESSAGE, BlockType.fromCode(1))
        assertEquals(BlockType.FILE, BlockType.fromCode(2))
        assertEquals(BlockType.CUSTOM_DATA, BlockType.fromCode(3))
        assertEquals(BlockType.EMBEDDING, BlockType.fromCode(4))
    }

    @Test(expected = IllegalArgumentException::class)
    fun fromCode_throwsForUnknownCode() {
        BlockType.fromCode(99.toByte())
    }
}

