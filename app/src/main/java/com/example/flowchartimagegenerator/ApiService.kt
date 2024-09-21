package com.example.flowchartimagegenerator

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

object Network {

    val logging: HttpLoggingInterceptor =
        HttpLoggingInterceptor(
            object: HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.v("ClarityHub", message)
                }

            }
        ).setLevel(HttpLoggingInterceptor.Level.BODY)

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        .addInterceptor(logging)
        .cache(null)
        .connectTimeout(50, TimeUnit.SECONDS)
        .readTimeout(50, TimeUnit.SECONDS)
        .writeTimeout(50, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)

    var retrofit : Retrofit? = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
//        .addConverterFactory(UnitConverterFactory)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl("https://1342-49-249-171-190.ngrok-free.app/")
        .client(httpClient.build())
        .build()


}

interface ApiService {

    @POST("generate-flowchart-image")
    fun getImage(
        @Body
        bodyModel: BodyModel
    ): Deferred<Response<DataModel>>
}

object Api {
    var retrofitService: ApiService? = Network.retrofit!!.create(ApiService::class.java)

    fun createRetrofitService(){
        retrofitService = Network.retrofit!!.create(ApiService::class.java)
    }
}