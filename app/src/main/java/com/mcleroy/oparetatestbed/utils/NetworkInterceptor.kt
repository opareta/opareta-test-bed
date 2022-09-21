package com.mcleroy.oparetatestbed.utils

import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

class NetworkInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjIxZTZjMGM2YjRlMzA5NTI0N2MwNjgwMDAwZTFiNDMxODIzODZkNTAiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vb3BhcmV0YS0zMTk5ZCIsImF1ZCI6Im9wYXJldGEtMzE5OWQiLCJhdXRoX3RpbWUiOjE2NjMwMTA5NTgsInVzZXJfaWQiOiJSbVFSTGcwdFMwVm8ycW5zTTh1eUFsUnZ4bUgyIiwic3ViIjoiUm1RUkxnMHRTMFZvMnFuc004dXlBbFJ2eG1IMiIsImlhdCI6MTY2Mzc1ODEzMSwiZXhwIjoxNjYzNzYxNzMxLCJwaG9uZV9udW1iZXIiOiIrMjM0NzA4MDg4NzAyOSIsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsicGhvbmUiOlsiKzIzNDcwODA4ODcwMjkiXX0sInNpZ25faW5fcHJvdmlkZXIiOiJwaG9uZSJ9fQ.eNx5RTW_n3Y9BoHfhUvz09UjNU0dS4psudYsjEYjuGgqYMKVqYLMV4IPdNZM7tsa4H-t4UgLYvHcje_T7Kxq4_iHBAfjNrZ3vWjmaBUQpfUnncxx9liAcagJPrn9PfzbCfPgUz_lCUS2NHJnI12Vq0eL3KIPR5upvkTmt1cNUpW8mjKeaDbwhehQXbmyC244VfKq4uFwnCEzrN_k0hBVC6WXA83jY6FmRMoL8Xy3mWZVks84DspBIzYFCAymSSaSgUqOwrz4ct4U4vb2-NPvBFPL65JYC-3gjdF4M6FsDKL17wYb4QUFFw3Z6f7yV6ljpC9NKTVYRg5xwC89qXawiA"
        val original = chain.request()
        val sTimeout = original.header("timeout")
        val timeout =
            if (sTimeout != null && TextUtils.isDigitsOnly(sTimeout)) sTimeout.toInt() else 60
        val requestBuilder = original.newBuilder()
            .method(original.method, original.body)
            .addHeader("Authorization", token)
        val newRequest = requestBuilder.build()
        val response = chain.withConnectTimeout(timeout, TimeUnit.SECONDS).proceed(newRequest)
        return response.newBuilder()
            .header("Cache-Control", "no-cache,no-store, must-revalidate")
            .removeHeader("ETag")
            .build()
    }
}