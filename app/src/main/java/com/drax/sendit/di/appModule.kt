package com.drax.sendit.di

import com.drax.sendit.data.db.AppDB
import com.drax.sendit.data.repo.AuthRepositoryImpl
import com.drax.sendit.data.repo.ConnectionRepositoryImpl
import com.drax.sendit.data.repo.DeviceRepositoryImpl
import com.drax.sendit.data.repo.PushRepositoryImpl
import com.drax.sendit.data.repo.RegistryRepositoryImpl
import com.drax.sendit.data.repo.TransactionRepositoryImpl
import com.drax.sendit.data.repo.UserRepositoryImpl
import com.drax.sendit.data.service.Analytics
import com.drax.sendit.data.service.NotificationBuilder
import com.drax.sendit.data.service.NotificationUtil
import com.drax.sendit.data.service.PushProcessor
import com.drax.sendit.di.builder.Json
import com.drax.sendit.domain.network.ApiService
import com.drax.sendit.domain.network.AppRetrofit
import com.drax.sendit.domain.network.AuthInterceptor
import com.drax.sendit.domain.network.ErrorHandlerInterceptor
import com.drax.sendit.domain.network.HeaderInterceptor
import com.drax.sendit.domain.repo.AuthRepository
import com.drax.sendit.domain.repo.ConnectionRepository
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.PushRepository
import com.drax.sendit.domain.repo.RegistryRepository
import com.drax.sendit.domain.repo.TransactionRepository
import com.drax.sendit.domain.repo.UserRepository
import com.drax.sendit.view.connections.ConnectionsVM
import com.drax.sendit.view.connections.unpair.UnpairVM
import com.drax.sendit.view.login.LoginVM
import com.drax.sendit.view.main.MainVM
import com.drax.sendit.view.messages.MessagesVM
import com.drax.sendit.view.qr.QrVM
import com.drax.sendit.view.scanner.ScannerVM
import com.drax.sendit.view.shareContent.ShareContentVM
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    //DB
    single                     {   Json().build() }


    single                     {   AppDB.get(androidContext()) }
    single                     {   get<AppDB>().registryDao() }
    single                     {   get<AppDB>().transactionDao() }
    single                     {   get<AppDB>().connectionDao() }
    single                     {   get<AppDB>().authDao() }

    viewModel                  {  ConnectionsVM(get(),get(),get(),get())  }
    viewModel                  {  QrVM(get(),get())  }
    viewModel                  {  MessagesVM(get(),get(),get())  }
    viewModel                  {  LoginVM(get(),get(),get(),get(),get())  }
    viewModel                  {  MainVM(get(), get())  }
    viewModel                  {  ScannerVM()  }
    viewModel                  {  ShareContentVM(get(), get(), get())  }
    viewModel                  {  UnpairVM(get())  }

    single<DeviceRepository>  { DeviceRepositoryImpl(get(), get()) }
    single<PushRepository>     { PushRepositoryImpl(get(),get(),get()) }
    single<RegistryRepository> { RegistryRepositoryImpl(get(), get()) }
    single<UserRepository>     { UserRepositoryImpl(get()) }
    single<TransactionRepository>     { TransactionRepositoryImpl(get(), get()) }
    single<AuthRepository>     { AuthRepositoryImpl(get(), get()) }
    single<ConnectionRepository>     { ConnectionRepositoryImpl(get(), get()) }

    single<ApiService>         {   AppRetrofit(get(), get(), get(), get()).getRetrofitClient()
        .create(ApiService::class.java)}
    single                     { AuthInterceptor(get()) }
    single                     { HeaderInterceptor( get()) }
    single                     { ErrorHandlerInterceptor( get(), get()) }

    single                     { PushProcessor(get(),get(),get(),get(),)}
    single                     { NotificationUtil(androidContext(), get()) }
    single                     { NotificationBuilder(get(),) }
    single                     { Analytics(Firebase.analytics) }

}
