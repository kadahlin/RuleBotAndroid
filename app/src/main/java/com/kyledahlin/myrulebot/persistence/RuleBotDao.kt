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

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface RuleBotDao {
    @Insert(onConflict = REPLACE)
    fun insertGuilds(vararg guilds: Guild)

    @Insert(onConflict = REPLACE)
    fun insertMembers(vararg members: Member)

    @Query("SELECT * FROM guilds")
    suspend fun getGuilds(): List<Guild>

    @Query("SELECT * FROM members WHERE guildId = :guildId")
    suspend fun getMembersForGuild(guildId: Long): List<Member>
}