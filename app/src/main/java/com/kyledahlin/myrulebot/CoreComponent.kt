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
package com.kyledahlin.myrulebot

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kyledahlin.myrulebot.backend.RuleBotApi
import com.kyledahlin.myrulebot.backend.RuleBotApiImpl
import com.kyledahlin.myrulebot.backend.RuleBotService
import com.kyledahlin.myrulebot.persistence.*
import com.kyledahlin.myrulebot.rules.RuleClassViewModel
import com.kyledahlin.myrulebot.rules.RuleRepo
import com.kyledahlin.myrulebot.rules.RuleRepoImpl
import dagger.*
import dagger.multibindings.IntoMap
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Module
internal abstract class InterfaceModule {
    @Binds
    @Singleton
    abstract fun bindsRuleRepo(ruleRepoImpl: RuleRepoImpl): RuleRepo

    @Binds
    @Singleton
    abstract fun bindsRuleBotApi(ruleBotApiImpl: RuleBotApiImpl): RuleBotApi

    @Binds
    @Singleton
    abstract fun bindsTimeApi(timeApiImpl: TimeApiImpl): TimeApi

    @Binds
    @Singleton
    abstract fun bindsPreferences(preferencesImpl: PreferencesImpl): Preferences
}

@Module
internal class NetworkModule {
    companion object {
        @JvmStatic
        @Singleton
        @Provides
        fun providesRetrofit(): Retrofit {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)

            return Retrofit.Builder()
                .addConverterFactory(
                    Json(JsonConfiguration.Stable.copy(isLenient = true))
                        .asConverterFactory(MediaType.parse("application/json")!!)
                )
                .client(httpClient.build())
                .baseUrl(BuildConfig.RULEBOT_URL)
                .build()
        }

        @JvmStatic
        @Singleton
        @Provides
        fun providesRuleBotService(retrofit: Retrofit): RuleBotService = retrofit.create()
    }
}

@Module
internal class ViewModelModule {
    @Provides
    @Singleton
    fun provideViewModelFactory(
        providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ) = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return requireNotNull(providers[modelClass as Class<out ViewModel>]).get() as T
        }
    }

    @Provides
    @IntoMap
    @ViewModelKey(RuleClassViewModel::class)
    fun provideFeatureViewModel(repo: RuleRepo): ViewModel = RuleClassViewModel(repo)
}

@Singleton
@Component(modules = [ViewModelModule::class, InterfaceModule::class, NetworkModule::class, PersistenceModule::class])
interface CoreComponent {
    fun inject(activity: RuleSelectionActivity)
    fun providesRetrofit(): Retrofit
    fun providesRuleBotService(): RuleBotApi
    fun providesPreferences(): Preferences

    @Component.Builder
    interface Builder {
        fun build(): CoreComponent

        @BindsInstance
        fun applicationContext(context: Context): Builder
    }
}

@MapKey
@Target(AnnotationTarget.FUNCTION)
annotation class ViewModelKey(
    val value: KClass<out ViewModel>
)
