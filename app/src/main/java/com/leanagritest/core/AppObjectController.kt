package com.leanagritest.core


import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.leanagritest.BuildConfig
import com.leanagritest.LeanAgriApplication
import com.leanagritest.repository.local.AppDatabase
import com.leanagritest.repository.service.MoviesNetworkService
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


private const val READ_TIMEOUT = 30L
private const val WRITE_TIMEOUT = 30L
private const val CONNECTION_TIMEOUT = 30L
private const val CALL_TIMEOUT = 60L

class AppObjectController {

    companion object {

        @JvmStatic
        var INSTANCE: AppObjectController =
            AppObjectController()

        @JvmStatic
        lateinit var joshApplication: LeanAgriApplication
            private set

        @JvmStatic
        lateinit var appDatabase: AppDatabase
            private set

        @JvmStatic
        lateinit var retrofit: Retrofit
            private set

        @JvmStatic
        lateinit var moviesNetworkService: MoviesNetworkService
            private set

        @JvmStatic
        var uiHandler: Handler = Handler(Looper.getMainLooper())
            private set

        fun initLibrary(context: Context): AppObjectController {
            joshApplication = context as LeanAgriApplication
            appDatabase = AppDatabase.getDatabase(context)!!
            Stetho.initializeWithDefaults(context)


            val builder = OkHttpClient().newBuilder()
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .callTimeout(CALL_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(ApiKeyInterceptor())


            if (BuildConfig.DEBUG) {
                val logging =
                    HttpLoggingInterceptor { message -> Log.e("OkHttp", message) }.apply {
                        level = HttpLoggingInterceptor.Level.BODY

                    }
                builder.addInterceptor(logging)
                builder.addNetworkInterceptor(StethoInterceptor())
            }
            retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(builder.build())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .build()
            moviesNetworkService = retrofit.create(MoviesNetworkService::class.java)
            return INSTANCE
        }
    }

    class ApiKeyInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var request: Request = chain.request()
            val url: HttpUrl = request.url.newBuilder()
                .addQueryParameter("api_key", BuildConfig.MOVIE_DB_API_KEY).build()
            request = request.newBuilder().url(url).build()
            return chain.proceed(request)
        }
    }


}
