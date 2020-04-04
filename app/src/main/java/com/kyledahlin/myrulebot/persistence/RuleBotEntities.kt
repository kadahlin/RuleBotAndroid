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

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(tableName = "guilds")
data class Guild(
    @PrimaryKey val id: Long,
    val name: String
)

@Entity(
    tableName = "members",
    foreignKeys = [
        ForeignKey(
            entity = Guild::class,
            parentColumns = ["id"],
            childColumns = ["guildId"],
            onDelete = CASCADE
        )
    ],
    indices = [
        Index(value = ["guildId"])
    ]
)
data class Member(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "display_name") val displayName: String,
    val guildId: Long
)