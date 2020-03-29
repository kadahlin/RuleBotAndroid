package com.kyledahlin.myrulebot.backend

import com.kyledahlin.myrulebot.generateNameIds
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RuleBotApiTest {

    private lateinit var _mockService: RuleBotService
    private lateinit var _ruleRepo: RuleBotApi

    @Before
    fun setup() {
        _mockService = mock()
        _ruleRepo = RuleBotApiImpl(_mockService)
    }

    @Test
    fun `the rule repo should query the backend when the cache is not used`() = runBlocking {
        val guilds = generateNameIds(3)
        whenever(_mockService.getGuilds()).thenReturn(guilds)

        val result = _ruleRepo.getGuilds(useCache = false)
        assertEquals(result.size, guilds.size)
    }

    //TODO: this test should be failing as the cache is not implemented yet
    @Test
    fun `the rule repo should not query the backend when the cache is requested`() =
        runBlocking<Unit> {
            val result = _ruleRepo.getGuilds(useCache = true)
            verify(_mockService, never()).getGuilds()
        }
}