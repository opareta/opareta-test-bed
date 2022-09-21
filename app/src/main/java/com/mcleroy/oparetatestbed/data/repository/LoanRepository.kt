package com.mcleroy.oparetatestbed.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mcleroy.oparetatestbed.Opareta
import com.mcleroy.oparetatestbed.data.api.ServerResponse
import com.mcleroy.oparetatestbed.data.entity.LoanDurationEntity
import com.mcleroy.oparetatestbed.data.entity.LoanEntity
import com.mcleroy.oparetatestbed.data.entity.LoanRequestEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class LoanRepository {

    private val restApiInterface = Opareta.apiService

    fun requestLoan(body: HashMap<String, Any>): LiveData<ServerResponse.Resource<LoanRequestEntity>> {
        val loanEntityMutableLiveData: MutableLiveData<ServerResponse.Resource<LoanRequestEntity>> =
            MutableLiveData()
        val call: Call<LoanRequestEntity> = restApiInterface.requestLoan(body)
        loanEntityMutableLiveData.postValue(ServerResponse.Resource.loading(null))
        call.enqueue(object : Callback<LoanRequestEntity> {
            override fun onResponse(
                call: Call<LoanRequestEntity>,
                response: Response<LoanRequestEntity>
            ) {
                if (response.isSuccessful) loanEntityMutableLiveData.postValue(
                    ServerResponse.Resource.success(
                        response.body()
                    )
                ) else loanEntityMutableLiveData.postValue(
                    ServerResponse.Resource.error(
                        null,
                        ServerResponse.NetworkApiException.fromErrorBody(response.errorBody())
                    )
                )
                Timber.d(LoanRepository::class.java.name, "Successfully requested loan")
            }

            override fun onFailure(call: Call<LoanRequestEntity>, t: Throwable) {
                Timber.e(t, "Error getting loan")
                loanEntityMutableLiveData.postValue(
                    ServerResponse.Resource.error(
                        null, ServerResponse.NetworkApiException.createFromException(
                            Exception(t)
                        )
                    )
                )
            }
        })
        return loanEntityMutableLiveData
    }

    fun updateLoanRequest(
        uuid: String,
        body: HashMap<String, Any>
    ): LiveData<ServerResponse.Resource<LoanRequestEntity>> {
        val call: Call<LoanRequestEntity> = restApiInterface.updateLoanRequest(uuid, body)
        val loanEntityMutableLiveData: MutableLiveData<ServerResponse.Resource<LoanRequestEntity>> =
            MutableLiveData()
        loanEntityMutableLiveData.postValue(ServerResponse.Resource.loading(null))
        call.enqueue(object : Callback<LoanRequestEntity> {
            override fun onResponse(
                call: Call<LoanRequestEntity>,
                response: Response<LoanRequestEntity>
            ) {
                if (response.isSuccessful) loanEntityMutableLiveData.postValue(
                    ServerResponse.Resource.success(
                        response.body()
                    )
                ) else loanEntityMutableLiveData.postValue(
                    ServerResponse.Resource.error(
                        null,
                        ServerResponse.NetworkApiException.fromErrorBody(response.errorBody())
                    )
                )
                Timber.d(LoanRepository::class.java.name, "Successfully updated loan")
            }

            override fun onFailure(call: Call<LoanRequestEntity>, t: Throwable) {
                Timber.e(t, "Error updating loan")
                loanEntityMutableLiveData.postValue(
                    ServerResponse.Resource.error(
                        null, ServerResponse.NetworkApiException.createFromException(
                            Exception(t)
                        )
                    )
                )
            }
        })
        return loanEntityMutableLiveData
    }

    fun validatePin(
        agentUUID: String,
        body: HashMap<String, String>
    ): LiveData<ServerResponse.Resource<Map<String, Boolean>>> {
        val mutableLiveData: MutableLiveData<ServerResponse.Resource<Map<String, Boolean>>> =
            MutableLiveData<ServerResponse.Resource<Map<String, Boolean>>>()
        val loanEntityMutableLiveData: MutableLiveData<ServerResponse.Resource<LoanRequestEntity>> =
            MutableLiveData()
        val call: Call<Map<String, Boolean>> = restApiInterface.validatePin(agentUUID, body)
        loanEntityMutableLiveData.postValue(ServerResponse.Resource.loading(null))
        call.enqueue(object : Callback<Map<String, Boolean>> {
            override fun onResponse(
                call: Call<Map<String, Boolean>>,
                response: Response<Map<String, Boolean>>
            ) {
                if (response.isSuccessful) mutableLiveData.postValue(
                    ServerResponse.Resource.success(
                        response.body()
                    )
                ) else mutableLiveData.postValue(
                    ServerResponse.Resource.error(
                        null,
                        ServerResponse.NetworkApiException.fromErrorBody(response.errorBody())
                    )
                )
            }

            override fun onFailure(call: Call<Map<String, Boolean>>, t: Throwable) {
                Timber.e(t, "Error updating loan")
                mutableLiveData.postValue(
                    ServerResponse.Resource.error(
                        null, ServerResponse.NetworkApiException.createFromException(
                            Exception(t)
                        )
                    )
                )
            }
        })
        return mutableLiveData
    }

    fun getLoans(): LiveData<ServerResponse.Resource<MutableList<LoanEntity>>> {
        val loanEntityMutableLiveData: MutableLiveData<ServerResponse.Resource<LoanRequestEntity>> =
            MutableLiveData()
        val loansLiveData: MutableLiveData<ServerResponse.Resource<MutableList<LoanEntity>>> =
            MutableLiveData<ServerResponse.Resource<MutableList<LoanEntity>>>()
        restApiInterface.getLoans().enqueue(object : Callback<MutableList<LoanEntity>> {
            override fun onResponse(
                call: Call<MutableList<LoanEntity>>,
                response: Response<MutableList<LoanEntity>>
            ) {
                if (response.isSuccessful) loansLiveData.postValue(
                    ServerResponse.Resource.success(
                        response.body()
                    )
                ) else loansLiveData.postValue(
                    ServerResponse.Resource.error(
                        null,
                        ServerResponse.NetworkApiException.fromErrorBody(response.errorBody())
                    )
                )
            }

            override fun onFailure(call: Call<MutableList<LoanEntity>>, t: Throwable) {
                loanEntityMutableLiveData.postValue(
                    ServerResponse.Resource.error(
                        null, ServerResponse.NetworkApiException.createFromException(
                            Exception(t)
                        )
                    )
                )
            }
        })
        return loansLiveData
    }

    fun getLoanDurations(): LiveData<ServerResponse.Resource<MutableList<LoanDurationEntity>>> {
        val loansLiveData: MutableLiveData<ServerResponse.Resource<MutableList<LoanDurationEntity>>> =
            MutableLiveData<ServerResponse.Resource<MutableList<LoanDurationEntity>>>()
        restApiInterface.getLoanDurations()
            .enqueue(object : Callback<MutableList<LoanDurationEntity>> {
                override fun onResponse(
                    call: Call<MutableList<LoanDurationEntity>>,
                    response: Response<MutableList<LoanDurationEntity>>
                ) {
                    if (response.isSuccessful) loansLiveData.postValue(
                        ServerResponse.Resource.success(
                            response.body()
                        )
                    ) else loansLiveData.postValue(
                        ServerResponse.Resource.error(
                            null,
                            ServerResponse.NetworkApiException.fromErrorBody(response.errorBody())
                        )
                    )
                }

                override fun onFailure(call: Call<MutableList<LoanDurationEntity>>, t: Throwable) {
                    loansLiveData.postValue(
                        ServerResponse.Resource.error(
                            null, ServerResponse.NetworkApiException.createFromException(
                                Exception(t)
                            )
                        )
                    )
                }
            })
        return loansLiveData
    }

    fun getLoanRequest(uuid: String): LiveData<ServerResponse.Resource<LoanRequestEntity>> {
        val loansLiveData: MutableLiveData<ServerResponse.Resource<LoanRequestEntity>> =
            MutableLiveData<ServerResponse.Resource<LoanRequestEntity>>()
        restApiInterface.getLoanRequest(uuid).enqueue(object : Callback<LoanRequestEntity> {
            override fun onResponse(
                call: Call<LoanRequestEntity>,
                response: Response<LoanRequestEntity>
            ) {
                if (response.isSuccessful) loansLiveData.postValue(
                    ServerResponse.Resource.success(
                        response.body()
                    )
                ) else loansLiveData.postValue(
                    ServerResponse.Resource.error(
                        null,
                        ServerResponse.NetworkApiException.fromErrorBody(response.errorBody())
                    )
                )
            }

            override fun onFailure(call: Call<LoanRequestEntity>, t: Throwable) {
                loansLiveData.postValue(
                    ServerResponse.Resource.error(
                        null, ServerResponse.NetworkApiException.createFromException(
                            Exception(t)
                        )
                    )
                )
            }
        })
        return loansLiveData
    }

    fun getLoanRequests(): LiveData<ServerResponse.Resource<MutableList<LoanRequestEntity>>> {
        val loansLiveData: MutableLiveData<ServerResponse.Resource<MutableList<LoanRequestEntity>>> =
            MutableLiveData<ServerResponse.Resource<MutableList<LoanRequestEntity>>>()
        restApiInterface.getLoanRequests()
            .enqueue(object : Callback<MutableList<LoanRequestEntity>> {
                override fun onResponse(
                    call: Call<MutableList<LoanRequestEntity>>,
                    response: Response<MutableList<LoanRequestEntity>>
                ) {
                    if (response.isSuccessful) loansLiveData.postValue(
                        ServerResponse.Resource.success(
                            response.body()
                        )
                    ) else loansLiveData.postValue(
                        ServerResponse.Resource.error(
                            null,
                            ServerResponse.NetworkApiException.fromErrorBody(response.errorBody())
                        )
                    )
                }

                override fun onFailure(call: Call<MutableList<LoanRequestEntity>>, t: Throwable) {
                    loansLiveData.postValue(
                        ServerResponse.Resource.error(
                            null,
                            ServerResponse.NetworkApiException.createFromThrowable(t)
                        )
                    )
                }
            })
        return loansLiveData
    }

    companion object {
        @Volatile
        private var instance: LoanRepository? = null

        fun get(): LoanRepository {
            return instance ?: synchronized(this) {
                instance ?: LoanRepository()
            }
        }
    }
}