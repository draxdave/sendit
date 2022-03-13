package com.drax.sendit.di

import com.drax.sendit.data.db.AppDB
import com.drax.sendit.data.repo.DevicesRepositoryImp
import com.drax.sendit.data.repo.PushRepositoryImp
import com.drax.sendit.data.repo.RegistryRepositoryImpl
import com.drax.sendit.data.repo.UserRepositoryImpl
import com.drax.sendit.domain.network.ApiService
import com.drax.sendit.domain.network.AppRetrofit
import com.drax.sendit.domain.repo.DevicesRepository
import com.drax.sendit.domain.repo.PushRepository
import com.drax.sendit.domain.repo.RegistryRepository
import com.drax.sendit.domain.repo.UserRepository
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

    viewModel                  {  DevicesVM(get(),get())  }
    viewModel                  {  QrVM()  }
    viewModel                  {  TransmissionsVM()  }
    viewModel                  {  LoginVM(get())  }
    viewModel                  {  ProfileVM()  }
    viewModel                  {  MainVM(get())  }

    single<DevicesRepository>  { DevicesRepositoryImp(get()) }
    single<PushRepository>     { PushRepositoryImp(get(),get(),get()) }
    single<RegistryRepository> { RegistryRepositoryImpl(get()) }
    single<UserRepository>     { UserRepositoryImpl(get()) }

    single<ApiService>         {   AppRetrofit().getRetrofitClient().create(ApiService::class.java)     }

}
