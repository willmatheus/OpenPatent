package com.android.openpatent

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.metamask.androidsdk.DappMetadata
import io.metamask.androidsdk.Ethereum
import io.metamask.androidsdk.SDKOptions
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideEthereum(@ApplicationContext context: Context): Ethereum {
        return Ethereum(
            context = context,
            dappMetadata = DappMetadata(
                name = context.applicationInfo.name,
                url = "https://${context.applicationInfo.name}.com",
                iconUrl = "https://raw.githubusercontent.com/rezaiyan/MetamaskConnect/refs/heads/main/football-player-15446.png"
            ),
            sdkOptions = SDKOptions(infuraAPIKey = "58a3c6e99e634500a03a4ee09261e8a0")
        )
    }
}