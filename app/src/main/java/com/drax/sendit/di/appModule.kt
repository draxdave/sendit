package com.drax.sendit.di

import com.drax.sendit.data.db.AppDB
import com.drax.sendit.data.repo.*
import com.drax.sendit.data.service.NotificationBuilder
import com.drax.sendit.data.service.NotificationUtil
import com.drax.sendit.data.service.PushProcessor
import com.drax.sendit.di.builder.Gson
import com.drax.sendit.di.builder.Json
import com.drax.sendit.domain.network.*
import com.drax.sendit.domain.repo.*
import com.drax.sendit.view.main.MainVM
import com.drax.sendit.view.connections.ConnectionsVM
import com.drax.sendit.view.login.LoginVM
import com.drax.sendit.view.profile.ProfileVM
import com.drax.sendit.view.qr.QrVM
import com.drax.sendit.view.scanner.ScannerVM
import com.drax.sendit.view.transmissions.TransactionsVM
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    //DB
    single                     {   Gson().build() }
    single                     {   Json().build() }


    single                     {   AppDB.get(androidContext()) }
    single                     {   get<AppDB>().registryDao() }
    single                     {   get<AppDB>().transactionDao() }
    single                     {   get<AppDB>().connectionDao() }
    single                     {   get<AppDB>().authDao() }

    viewModel                  {  ConnectionsVM(get(),get(),get())  }
    viewModel                  {  QrVM(get(),get())  }
    viewModel                  {  TransactionsVM(get(),get(),get())  }
    viewModel                  {  LoginVM(get(),get(),get(),get(),)  }
    viewModel                  {  ProfileVM(get(), get())  }
    viewModel                  {  MainVM(get(), get(), get(), get(), get())  }
    viewModel                  {  ScannerVM()  }

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
    single                     { ErrorHandlerInterceptor( get()) }

    single                     { PushProcessor(get(),get(),get(),get(),)}
    single                     { NotificationUtil(androidContext(), get()) }
    single                     { NotificationBuilder(get(),) }

}
