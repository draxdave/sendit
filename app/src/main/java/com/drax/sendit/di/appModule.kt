package com.drax.sendit.di

import com.drax.sendit.data.db.AppDB
import com.drax.sendit.data.repo.DevicesRepositoryImp
import com.drax.sendit.data.repo.PushRepositoryImp
import com.drax.sendit.data.repo.RegistryRepository
import com.drax.sendit.domain.network.ApiService
import com.drax.sendit.domain.network.AppRetrofit
import com.drax.sendit.domain.repo.DevicesRepository
import com.drax.sendit.domain.repo.PushRepository
import com.drax.sendit.view.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    //DB
    single                     {   AppDB.get(androidContext()) }
    single                     {   get<AppDB>().devicesDao() }
    single                     {   get<AppDB>().registryDao() }

    viewModel                  {  MainViewModel(get(),get())  }

    single<DevicesRepository>     { DevicesRepositoryImp(get()) }
    single<PushRepository>     { PushRepositoryImp(get(),get(),get()) }
    single                       { RegistryRepository(get()) }

    single<ApiService>      {   AppRetrofit().getRetrofitClient().create(ApiService::class.java)     }

}
