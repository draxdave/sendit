package com.drax.sendit.di

import com.drax.sendit.data.db.AppDB
import com.drax.sendit.data.repo.*
import com.drax.sendit.domain.network.*
import com.drax.sendit.domain.repo.*
import com.drax.sendit.view.MainVM
import com.drax.sendit.view.devices.DevicesVM
import com.drax.sendit.view.login.LoginVM
import com.drax.sendit.view.profile.ProfileVM
import com.drax.sendit.view.qr.QrVM
import com.drax.sendit.view.transmissions.TransmissionsVM
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    //DB
    single                     {   AppDB.get(androidContext()) }
    single                     {   get<AppDB>().devicesDao() }
    single                     {   get<AppDB>().registryDao() }
    single                     {   get<AppDB>().userDao() }
    single                     {   get<AppDB>().transactionDao() }

    viewModel                  {  DevicesVM(get(),get())  }
    viewModel                  {  QrVM(get(),get())  }
    viewModel                  {  TransmissionsVM()  }
    viewModel                  {  LoginVM(get(),get(),get(),)  }
    viewModel                  {  ProfileVM(get(), get())  }
    viewModel                  {  MainVM(get())  }

    single<DevicesRepository>  { DevicesRepositoryImpl(get(), get()) }
    single<PushRepository>     { PushRepositoryImpl(get(),get(),get()) }
    single<RegistryRepository> { RegistryRepositoryImpl(get()) }
    single<UserRepository>     { UserRepositoryImpl(get()) }
    single<TransactionRepository>     { TransactionRepositoryImpl(get(), get()) }
    single<AuthRepository>     { AuthRepositoryImpl(get(), get(),get(),get()) }

    single<ApiService>         {   AppRetrofit(get(), get(), get()).getRetrofitClient()
        .create(ApiService::class.java)}
    single                     { AuthInterceptor(get()) }
    single                     { HeaderInterceptor( get()) }
    single                     { ErrorHandlerInterceptor( androidContext().resources) }

}
