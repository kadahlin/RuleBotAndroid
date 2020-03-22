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
package com.kyledahlin.reactions

import com.kyledahlin.myrulebot.backend.NameAndId
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonObject
import retrofit2.http.Body
import retrofit2.http.POST

internal interface ReactionService {
    @POST("rules/reactions/")
    suspend fun postReactionCommand(@Body command: Command): JsonObject
}

internal suspend fun ReactionService.list(guildId: String): ListResponse {
    val stringList = this.postReactionCommand(Command(guildId = guildId, action = "list"))
    return Json(JsonConfiguration.Stable.copy(isLenient = true)).parse(
        ListResponse.serializer(),
        stringList.toString()
    )
}

@Serializable
internal data class ListResponse(
    val emojis: List<NameAndId>,
    val addedEmojis: List<AddedEmoji>
)

@Serializable
internal data class AddedEmoji(
    val emojiName: String,
    val emojiId: String,
    val memberId: String
)

