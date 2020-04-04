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
package com.kyledahlin.myrulebot.backend

import com.kyledahlin.myrulebot.persistence.Guild
import com.kyledahlin.myrulebot.persistence.Member
import com.kyledahlin.myrulebot.persistence.Preferences
import com.kyledahlin.myrulebot.persistence.RuleBotDao
import javax.inject.Inject
import javax.inject.Singleton

interface RuleBotApi {
    suspend fun getGuilds(useCache: Boolean = true): List<NameAndId>

    suspend fun getMembers(guildId: Long, useCache: Boolean = true): List<NameAndId>
}

/**
 * Get general information about the running rule bot instance (caching to come later)
 */
@Singleton
internal class RuleBotApiImpl @Inject constructor(
    private val ruleBotService: RuleBotService,
    private val ruleBotDao: RuleBotDao,
    private val preferences: Preferences
) :
    RuleBotApi {
    override suspend fun getGuilds(useCache: Boolean): List<NameAndId> {
        return if (useCache && preferences.isApiCacheValid()) {
            ruleBotDao
                .getGuilds()
                .map { NameAndId(it.name, it.id) }
        } else {
            val guilds = ruleBotService.getGuilds()
            val guildEntities = guilds.map { Guild(it.id, it.name) }.toTypedArray()
            ruleBotDao.insertGuilds(*guildEntities)
            guilds.forEach { getMembers(it.id, useCache = false) }
            preferences.apiCachePopulated()
            guilds
        }
    }

    override suspend fun getMembers(guildId: Long, useCache: Boolean): List<NameAndId> {
        return if (useCache && preferences.isApiCacheValid()) {
            ruleBotDao
                .getMembersForGuild(guildId)
                .map { NameAndId(it.displayName, it.id) }
        } else {
            val members = ruleBotService.getGuildMembers(guildId.toString())
            val memberEntities = members.map { Member(it.id, it.name, guildId) }.toTypedArray()
            ruleBotDao.insertMembers(*memberEntities)
            preferences.apiCachePopulated()
            members
        }
    }
}