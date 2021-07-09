package com.pekyurek.emircan.presentation.core.extensions

import org.junit.Test

internal class LongKtTest {


    @Test
    fun `date to message format`() {
        assert(1625695079587.toMessageTimeFormat() == "00:57, 08/07/2021")
        assert(1625695079587.toMessageTimeFormat() != "00:57 8/07/2021")
        assert(1625695079587.toMessageTimeFormat() != "00:57, 08/7/2021")
        assert(1625695079587.toMessageTimeFormat() != "00:57, 8/7/2021")
        assert(1625695079587.toMessageTimeFormat() != "0:57, 08/07/2021")
    }
}