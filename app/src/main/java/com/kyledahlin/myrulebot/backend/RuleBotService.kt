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

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Get general information about the running rule bot instance
 */
internal interface RuleBotService {
    @GET("/guilds")
    suspend fun getGuilds(): List<NameAndId>

    @GET("/guilds/{guildId}")
    suspend fun getGuildMembers(@Path("guildId") guildId: String): List<NameAndId>
}

@Serializable
@Parcelize
data class NameAndId(
    val name: String,
    val id: Long
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        return if (other is NameAndId) {
            id == other.id
        } else {
            false
        }
    }
}