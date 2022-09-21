package com.mcleroy.oparetatestbed

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mcleroy.oparetatestbed.services.RestApiInterface
import com.mcleroy.oparetatestbed.utils.NetworkInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

class Opareta: Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        gson = GsonBuilder()
            .create()
        Timber.plant(if (BuildConfig.DEBUG) Timber.DebugTree() else CrashReportingTree())
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(NetworkInterceptor())
            .addInterceptor(loggingInterceptor)
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(RestApiInterface.httpUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        preferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        handler = Handler(Looper.getMainLooper())
    }

    /** A tree which logs important information for crash reporting.  */
    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }
        }
    }

    companion object {

        lateinit var context: Context
        lateinit var gson: Gson
        lateinit var retrofit: Retrofit
        lateinit var handler: Handler
        lateinit var preferences: SharedPreferences
        lateinit var okHttpClient: OkHttpClient

        val apiService: RestApiInterface
            get() = retrofit.create(RestApiInterface::class.java)
    }
}