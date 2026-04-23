package com.memoryvault.core

import org.junit.Assert.assertEquals
import org.junit.Test

class VectorUtilsTest {

    @Test
    fun cosineSimilarity_returnsOneForIdenticalVectors() {
        val a = floatArrayOf(1f, 2f, 3f)
        val b = floatArrayOf(1f, 2f, 3f)

        assertEquals(1f, VectorUtils.cosineSimilarity(a, b), 1e-6f)
    }

    @Test
    fun cosineSimilarity_returnsZeroForOrthogonalVectors() {
        val a = floatArrayOf(1f, 0f)
        val b = floatArrayOf(0f, 1f)

        assertEquals(0f, VectorUtils.cosineSimilarity(a, b), 1e-6f)
    }

    @Test
    fun cosineSimilarity_returnsZeroWhenEitherVectorHasZeroNorm() {
        val a = floatArrayOf(0f, 0f, 0f)
        val b = floatArrayOf(1f, 2f, 3f)

        assertEquals(0f, VectorUtils.cosineSimilarity(a, b), 1e-6f)
    }

    @Test(expected = IllegalArgumentException::class)
    fun cosineSimilarity_throwsForMismatchedDimensions() {
        VectorUtils.cosineSimilarity(floatArrayOf(1f), floatArrayOf(1f, 2f))
    }
}

