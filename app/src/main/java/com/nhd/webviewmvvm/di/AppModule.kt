package com.nhd.webviewmvvm.di

import android.app.Application
import android.provider.ContactsContract.Data
import com.nhd.webviewmvvm.usecase.ClipboardUseCase
import com.nhd.webviewmvvm.usecase.WebPackageUseCase
import com.nhd.webviewmvvm.viewmodels.DataViewModel
import com.nhd.webviewmvvm.viewmodels.DeviceViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideWebPackageUseCase(application: Application): WebPackageUseCase {
        return WebPackageUseCase(application)
    }

    @Provides
    fun provideClipboardUseCase(application: Application): ClipboardUseCase {
        return ClipboardUseCase(application)
    }

    @Provides
    fun provideDataViewModel(application: Application): DataViewModel {
        return DataViewModel(provideClipboardUseCase(application), application)
    }

    @Provides
    fun provideDeviceViewModel(application: Application): DeviceViewModel {
        return DeviceViewModel(application)
    }

}