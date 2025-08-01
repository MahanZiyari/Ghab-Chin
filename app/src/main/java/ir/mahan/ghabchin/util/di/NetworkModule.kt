package ir.mahan.ghabchin.util.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.mahan.ghabchin.data.network.ApiServices
import ir.mahan.ghabchin.util.ACCEPT_VERSION
import ir.mahan.ghabchin.util.API_KEY
import ir.mahan.ghabchin.util.AUTHORIZATION
import ir.mahan.ghabchin.util.BASE_URL
import ir.mahan.ghabchin.util.CLIENT_ID
import ir.mahan.ghabchin.util.CONNECTION_TIME
import ir.mahan.ghabchin.util.NAMED_PING
import ir.mahan.ghabchin.util.PING_INTERVAL
import ir.mahan.ghabchin.util.V1
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, gson: Gson, client: OkHttpClient): ApiServices =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiServices::class.java)

    @Provides
    @Singleton
    fun provideBaseUrl() = BASE_URL

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideClient(
        timeout: Long, @Named(NAMED_PING) ping: Long, interceptor: HttpLoggingInterceptor
    ) = OkHttpClient.Builder()

        .addInterceptor { chain ->
            chain.proceed(chain.request().newBuilder().also {
                it.addHeader(AUTHORIZATION, "$CLIENT_ID $API_KEY")
                it.addHeader(ACCEPT_VERSION, V1)
            }.build())// end of proceed
        }.also { client ->
            client.addInterceptor(interceptor)
        }
        .writeTimeout(timeout, TimeUnit.SECONDS)
        .readTimeout(timeout, TimeUnit.SECONDS)
        .connectTimeout(timeout, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .pingInterval(ping, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideTimeout() = CONNECTION_TIME

    @Provides
    @Singleton
    @Named(NAMED_PING)
    fun providePingInterval() = PING_INTERVAL
}