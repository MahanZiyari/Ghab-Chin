package ir.mahan.ghabchin.util.di

import android.app.DownloadManager
import android.app.WallpaperManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object managersModule {

    @Provides
    @Singleton
    fun provideWallpaperManager(@ApplicationContext context: Context) = WallpaperManager.getInstance(context)

    @Provides
    @Singleton
    fun provideDownloadManager(@ApplicationContext context: Context) = context.getSystemService(
        Context.DOWNLOAD_SERVICE) as DownloadManager

}