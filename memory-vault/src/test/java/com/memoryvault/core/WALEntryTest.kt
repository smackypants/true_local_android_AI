package com.memoryvault.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.util.UUID

class WALEntryTest {

    @Test
    fun fromCode_returnsExpectedOperationForKnownCodes() {
        assertEquals(WALOperation.INSERT, WALOperation.fromCode(1))
        assertEquals(WALOperation.DELETE, WALOperation.fromCode(2))
        assertEquals(WALOperation.UPDATE, WALOperation.fromCode(3))
    }

    @Test(expected = IllegalArgumentException::class)
    fun fromCode_throwsForUnknownCode() {
        WALOperation.fromCode(99.toByte())
    }

    @Test
    fun toBytesAndFromBytes_roundTripWithDataAndCommittedFlag() {
        val entry = WALEntry(
            operation = WALOperation.INSERT,
            timestamp = 1710000000L,
            blockId = UUID.fromString("12345678-1234-5678-9abc-def012345678"),
            data = byteArrayOf(10, 20, 30),
            committed = true
        )

        val decoded = WALEntry.fromBytes(entry.toBytes())

        assertEquals(entry, decoded)
    }

    @Test
    fun toBytesAndFromBytes_roundTripWithNullData() {
        val entry = WALEntry(
            operation = WALOperation.DELETE,
            timestamp = 1710000001L,
            blockId = UUID.fromString("87654321-4321-8765-cba9-876543210fed"),
            data = null,
            committed = false
        )

        val decoded = WALEntry.fromBytes(entry.toBytes())

        assertEquals(entry, decoded)
    }

    @Test
    fun equalsAndHashCode_compareArrayContentNotReference() {
        val id = UUID.fromString("aaaaaaaa-1111-2222-3333-bbbbbbbbbbbb")
        val first = WALEntry(WALOperation.UPDATE, 1L, id, byteArrayOf(1, 2, 3), committed = true)
        val second = WALEntry(WALOperation.UPDATE, 1L, id, byteArrayOf(1, 2, 3), committed = true)
        val third = WALEntry(WALOperation.UPDATE, 1L, id, byteArrayOf(1, 2, 4), committed = true)

        assertEquals(first, second)
        assertEquals(first.hashCode(), second.hashCode())
        assertNotEquals(first, third)
    }
}
