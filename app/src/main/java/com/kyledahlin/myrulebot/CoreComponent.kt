package com.kyledahlin.myrulebot

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kyledahlin.myrulebot.rules.RuleClassViewModel
import com.kyledahlin.myrulebot.rules.RuleRepo
import com.kyledahlin.myrulebot.rules.RuleRepoImpl
import dagger.*
import dagger.multibindings.IntoMap
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Module
abstract class CoreModule {
    @Binds
    @Singleton
    abstract fun bindsRuleRepo(ruleRepoImpl: RuleRepoImpl): RuleRepo
}

@Module
class ViewModelModule {
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
@Component(modules = [ViewModelModule::class, CoreModule::class])
interface CoreComponent {
    fun inject(activity: RuleSelectionActivity)

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
