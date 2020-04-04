package com.kyledahlin.myrulebot.backend

import com.kyledahlin.myrulebot.generateNameIds
import com.kyledahlin.myrulebot.persistence.Preferences
import com.kyledahlin.myrulebot.persistence.RuleBotDao
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class RuleBotApiTest {

    @Mock
    private lateinit var _mockService: RuleBotService

    @Mock
    private lateinit var _mockApi: RuleBotApi

    @Mock
    private lateinit var _mockPrefs: Preferences

    @Mock
    private lateinit var _mockDao: RuleBotDao

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        _mockApi = RuleBotApiImpl(_mockService, _mockDao, _mockPrefs)
    }

    @Test
    fun `the rule repo should query the backend when the cache is not used`() = runBlocking {
        whenever(_mockPrefs.isApiCacheValid()).thenReturn(true)
        val guilds = generateNameIds(3)
        whenever(_mockService.getGuilds()).thenReturn(guilds)
        val members = generateNameIds(1)
        whenever(_mockService.getGuildMembers(any())).thenReturn(members)

        val result = _mockApi.getGuilds(useCache = false)
        assertEquals(result.size, guilds.size)

        val memberResult = _mockApi.getMembers(useCache = false, guildId = 1)
        assertEquals(members.size, memberResult.size)
    }

    @Test
    fun `the rule repo should not query the backend when the cache is requested and cache is valid`() =
        runBlocking<Unit> {
            whenever(_mockPrefs.isApiCacheValid()).thenReturn(true)

            whenever(_mockDao.getGuilds()).thenReturn(emptyList())
            _mockApi.getGuilds(useCache = true)
            verify(_mockService, never()).getGuilds()

            whenever(_mockDao.getMembersForGuild(any())).thenReturn(emptyList())
            _mockApi.getMembers(useCache = true, guildId = 1)
            verify(_mockService, never()).getGuildMembers(any())
        }

    @Test
    fun `the rule repo should query the backend when the cache is requested BUT cache is invalid`() =
        runBlocking<Unit> {
            whenever(_mockPrefs.isApiCacheValid()).thenReturn(false)

            whenever(_mockService.getGuilds()).thenReturn(emptyList())
            _mockApi.getGuilds(useCache = true)
            verify(_mockDao, never()).getGuilds()

            whenever(_mockService.getGuildMembers(any())).thenReturn(emptyList())
            _mockApi.getMembers(useCache = true, guildId = 1)
            verify(_mockDao, never()).getMembersForGuild(any())
        }
}