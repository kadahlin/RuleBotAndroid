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

import javax.inject.Inject
import javax.inject.Singleton

//This exists to support clean interface usage, to replace inner calls to things like System.currentTimeMillis
interface TimeApi {
    fun getCurrentTime(): Long
}

@Singleton
internal class TimeApiImpl @Inject constructor() : TimeApi {
    override fun getCurrentTime() = System.currentTimeMillis()
}