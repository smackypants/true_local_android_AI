package com.memoryvault.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TextTokenizerTest {

    @Test
    fun tokenize_normalizesAndRemovesStopWords() {
        val tokens = TextTokenizer.tokenize("This is, A test! with numbers 123 and x y.")

        assertEquals(listOf("test", "numbers", "123"), tokens)
    }

    @Test
    fun tokenize_canKeepStopWordsWhenRequested() {
        val tokens = TextTokenizer.tokenize("The cat and the dog", removeStopWords = false)

        assertEquals(listOf("the", "cat", "and", "the", "dog"), tokens)
    }

    @Test
    fun tokenize_returnsEmptyForBlankOrPunctuationOnlyInput() {
        assertTrue(TextTokenizer.tokenize("").isEmpty())
        assertTrue(TextTokenizer.tokenize("...,,,!!!").isEmpty())
    }
}
