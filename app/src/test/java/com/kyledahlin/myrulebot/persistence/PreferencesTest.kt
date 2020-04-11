/*
*Copyright 2020 Kyle Dahlin
*
*Licensed under the Apache License, Version 2.0 (the "License");
*you may not use this file except in compliance with the License.
*You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing, software
*distributed under the License is distributed on an "AS IS" BASIS,
*WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*See the License for the specific language governing permissions and
*limitations under the License.
*/
package com.kyledahlin.myrulebot.persistence

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(minSdk = Build.VERSION_CODES.P, maxSdk = Build.VERSION_CODES.P)
@RunWith(RobolectricTestRunner::class)
class PreferencesTest {

    private lateinit var _prefs: Preferences
    private lateinit var _mockSharedPrefs: SharedPreferences
    private lateinit var _mockTimeApi: TimeApi


    @Before
    fun setup() {
        _mockSharedPrefs = ApplicationProvider.getApplicationContext<Context>()
            .getSharedPreferences("testing", Context.MODE_PRIVATE)
        _mockTimeApi = mock()
        _prefs = PreferencesImpl(_mockSharedPrefs, _mockTimeApi)
    }

    @Test
    fun `the cache should be invalid initially`() {
        assert(!_prefs.isApiCacheValid())
    }

    @Test
    fun `the cache should be valid within the timeout`() {
        whenever(_mockTimeApi.getCurrentTime()).thenReturn(0)
        _prefs.apiCachePopulated()
        whenever(_mockTimeApi.getCurrentTime()).thenReturn(_prefs.cacheTimeout - 1)
        assert(_prefs.isApiCacheValid())
    }

    @Test
    fun `the cache should be invalid after the timeout has passed`() {
        whenever(_mockTimeApi.getCurrentTime()).thenReturn(0)
        _prefs.apiCachePopulated()
        whenever(_mockTimeApi.getCurrentTime()).thenReturn(_prefs.cacheTimeout + 1)
        assert(!_prefs.isApiCacheValid())
    }
}

