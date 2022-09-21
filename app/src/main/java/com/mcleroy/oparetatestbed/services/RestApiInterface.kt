package com.mcleroy.oparetatestbed.services

import com.mcleroy.oparetatestbed.BuildConfig
import com.mcleroy.oparetatestbed.data.entity.LoanDurationEntity
import com.mcleroy.oparetatestbed.data.entity.LoanEntity
import com.mcleroy.oparetatestbed.data.entity.LoanRequestEntity
import okhttp3.HttpUrl
import retrofit2.Call
import retrofit2.http.*

interface RestApiInterface {
    
    @POST(value = "/api/agents/{uuid}/validate-pin")
    fun validatePin(
        @Path("uuid") uuid: String,
        @Body body: java.util.HashMap<String, String>
    ): Call<Map<String, Boolean>>
    
    /* LOANS */
    @GET(value = "/api/loans")
    fun getAllLoans(): Call<MutableList<LoanEntity>>

    @GET(value = "/api/loans/requests")
    fun getLoanRequests(): Call<MutableList<LoanRequestEntity>>

    @POST(value = "/api/loans/requests")
    fun requestLoan(@Body body: HashMap<String, Any>): Call<LoanRequestEntity>

    @PATCH(value = "/api/loans/requests/{uuid}")
    fun updateLoanRequest(
        @Path("uuid") uuid: String,
        @Body body: HashMap<String, Any>
    ): Call<LoanRequestEntity>

    @GET(value = "/api/loans/requests/{uuid}")
    fun getLoanRequest(@Path("uuid") uuid: String): Call<LoanRequestEntity>

    @GET(value = "/api/loans/durations")
    fun getLoanDurations(): Call<MutableList<LoanDurationEntity>>

    @GET(value = "/api/loans")
    fun getLoans(): Call<MutableList<LoanEntity>>

    companion object {
        private const val host: String = BuildConfig.HOST
        val httpUrl: String
            get() {
                val httpUrl = HttpUrl.Builder()
                    .scheme(BuildConfig.HTTP_SCHEME)
                    .host(host)
                    .addPathSegment("v1")
                    .build()
                return "$httpUrl/"
            }
    }
}