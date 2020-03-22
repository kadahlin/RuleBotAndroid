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
package com.kyledahlin.myrulebot.rules

import android.content.Context
import android.content.pm.PackageManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Get all of the rules currently in the application that are registered with [RuleActivity]
 */
interface RuleRepo {
    fun getRuleEntries(): List<RuleEntry>
}

@Singleton
class RuleRepoImpl @Inject constructor(private val _context: Context) : RuleRepo {

    override fun getRuleEntries(): List<RuleEntry> {
        //create a new context in case any of the dynamic features have updated
        val newContext = _context.createPackageContext(_context.packageName, 0)

        val activities = newContext.packageManager.getPackageInfo(
            _context.packageName,
            PackageManager.GET_ACTIVITIES
        )
        return activities.activities
            .mapNotNull { info ->
                val activityName = info.name
                val activityClass = Class.forName(activityName)
                val annotation = activityClass.getDeclaredAnnotation(RuleActivity::class.java)
                val ruleName = annotation?.name

                if (ruleName != null)
                    ruleName to activityName
                else
                    null
            }.map { RuleEntry(it.first, it.second) }
            .distinct()
    }
}

data class RuleEntry(
    val ruleName: String,
    val activityName: String
) {
    override fun equals(other: Any?): Boolean {
        return if (other is RuleEntry) {
            other.ruleName == ruleName
        } else {
            false
        }
    }
}