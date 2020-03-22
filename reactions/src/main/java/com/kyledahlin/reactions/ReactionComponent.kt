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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kyledahlin.myrulebot.CoreComponent
import com.kyledahlin.myrulebot.ViewModelKey
import com.kyledahlin.myrulebot.backend.RuleBotApi
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Provider
import javax.inject.Scope

@Scope
@Retention
annotation class ReactionScope

@Module
internal object ViewModelModule {
    @Provides
    @ReactionScope
    fun provideViewModelFactory(
        providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ) = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return requireNotNull(providers[modelClass as Class<out ViewModel>]).get() as T
        }
    }

    @Provides
    @IntoMap
    @ReactionScope
    @ViewModelKey(ReactionViewModel::class)
    fun provideFeatureViewModel(
        reactionService: ReactionService,
        ruleBotApi: RuleBotApi
    ): ViewModel = ReactionViewModelImpl(reactionService, ruleBotApi)
}

@Module
internal object ReactionModule {
    @JvmStatic
    @Provides
    @ReactionScope
    fun providesReactionService(retrofit: Retrofit): ReactionService = retrofit.create()
}

@Component(
    dependencies = [CoreComponent::class],
    modules = [ReactionModule::class, ViewModelModule::class]
)
@ReactionScope
internal interface ReactionComponent {
    fun inject(reactionFragment: ReactionFragment)
    fun inject(reactionActivity: ReactionActivity)
    fun inject(emojiSelectionPage: EmojiSelectionPage)
    fun inject(guildSelectionPage: GuildSelectionPage)
    fun inject(memberSelectionPage: MemberSelectionPage)
}