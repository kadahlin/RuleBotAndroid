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

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Singleton

interface Preferences {
    //does the cache contains relevant and up to date data
    fun isApiCacheValid(): Boolean

    //Data has been saved to the cache and this timestamp should be saved
    fun apiCachePopulated()
}

private const val API_CACHE_KEY = "api_cache_timestamp"
private const val ONE_DAY_MILLISECONDS = 1000 * 60 * 60 * 24

@Singleton
internal class PreferencesImpl @Inject constructor(
    private val _prefs: SharedPreferences,
    private val _timeApi: TimeApi
) : Preferences {

    override fun isApiCacheValid(): Boolean {
        val lastTime = _prefs.getLong(API_CACHE_KEY, -1L)
        return lastTime != -1L && !hasOneDayPassed(lastTime)
    }

    fun hasOneDayPassed(lastTime: Long): Boolean {
        return (System.currentTimeMillis() - lastTime) >= ONE_DAY_MILLISECONDS
    }

    override fun apiCachePopulated() {
        _prefs.edit { putLong(API_CACHE_KEY, System.currentTimeMillis()) }
    }
}