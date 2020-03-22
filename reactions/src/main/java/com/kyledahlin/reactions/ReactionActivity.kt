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

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kyledahlin.myrulebot.coreComponent
import com.kyledahlin.myrulebot.rules.RuleActivity

@RuleActivity(name = "reactions")
class ReactionActivity : AppCompatActivity() {

    private val _component by lazy {
        DaggerReactionComponent.builder().coreComponent(coreComponent()).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reaction)
    }

    internal fun getComponent() = _component
}
