package com.drax.sendit.di

import android.content.Context
import com.drax.sendit.data.db.AppDB
import com.drax.sendit.data.db.AuthDao
import com.drax.sendit.data.db.ConnectionDao
import com.drax.sendit.data.db.RegistryDao
import com.drax.sendit.data.db.TransactionsDao
import com.drax.sendit.data.repo.AuthRepositoryImpl
import com.drax.sendit.data.repo.ConnectionRepositoryImpl
import com.drax.sendit.data.repo.DeviceRepositoryImpl
import com.drax.sendit.data.repo.PushRepositoryImpl
import com.drax.sendit.data.repo.RegistryRepositoryImpl
import com.drax.sendit.data.repo.TransactionRepositoryImpl
import com.drax.sendit.data.repo.UserRepositoryImpl
import com.drax.sendit.data.service.Analytics
import com.drax.sendit.di.builder.Json
import com.drax.sendit.domain.network.ApiService
import com.drax.sendit.domain.network.AppRetrofit
import com.drax.sendit.domain.repo.AuthRepository
import com.drax.sendit.domain.repo.ConnectionRepository
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.PushRepository
import com.drax.sendit.domain.repo.RegistryRepository
import com.drax.sendit.domain.repo.TransactionRepository
import com.drax.sendit.domain.repo.UserRepository
import com.drax.sendit.view.login.SsoHandler
import com.drax.sendit.view.util.DeviceInfoHelper
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {


    @Binds
    abstract fun bindDeviceRepository(impl: DeviceRepositoryImpl): DeviceRepository

    @Binds
    abstract fun bindPushRepository(impl: PushRepositoryImpl): PushRepository

    @Binds
    abstract fun bindRegistryRepository(impl: RegistryRepositoryImpl): RegistryRepository

    @Binds
    abstract fun bindTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository

    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindConnectionRepository(impl: ConnectionRepositoryImpl): ConnectionRepository

    @Binds
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}

@Module
@InstallIn(SingletonComponent::class)
class networkModule {
    @Provides
    fun bindNetwork(retrofit: AppRetrofit): ApiService = retrofit
        .getRetrofitClient().create(ApiService::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
class dbModule {

    @Provides
    fun bindDb(@ApplicationContext context: Context): AppDB = AppDB.get(context)

    @Provides
    fun bindRegistryDao(db: AppDB): RegistryDao = db.registryDao()

    @Provides
    fun bindTransactionsDao(db: AppDB): TransactionsDao = db.transactionDao()

    @Provides
    fun bindConnectionDao(db: AppDB): ConnectionDao = db.connectionDao()

    @Provides
    fun bindAuthDao(db: AppDB): AuthDao = db.authDao()
}

@Module
@InstallIn(SingletonComponent::class)
class otherModule {

    companion object {
        @Provides
        fun bindAnalytics(@ApplicationContext context: Context): Analytics =
            Analytics(FirebaseAnalytics.getInstance(context))

        @Provides
        fun bindJson(): kotlinx.serialization.json.Json = Json().build()

        @Provides
        fun bindDispatcher(): CoroutineDispatcher = Dispatchers.Default

        @Provides
        fun bindDeviceInfoHelper(
            @ApplicationContext context: Context
        ): DeviceInfoHelper = DeviceInfoHelper(context)

        @Provides
        fun bindFirebaseID(
            @ApplicationContext context: Context
        ): FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

        @Provides
        fun providesGoogleSsoHandler(
            @ApplicationContext context: Context
        ): SsoHandler{
            return SsoHandler(context)
        }
    }
}