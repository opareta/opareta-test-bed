package com.mcleroy.oparetatestbed.data.api

import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException

open class ServerResponse {
    
    open class Resource<out T>(
        val isLoading: Boolean, val data: T?, val ex: NetworkApiException?) {
        val errorMessage: String?
            get() = ex?.message

        companion object {
            fun <T> loading(data: T?): Resource<T> {
                return Resource(true, data, null)
            }

            fun <T> success(data: T?): Resource<T> {
                return Resource(false, data, null)
            }

            fun <T> error(data: T?, ex: NetworkApiException): Resource<T> {
                return Resource(false, data, ex)
            }
        }
    }

    class NetworkApiException : Exception {
        val statusCode: Int

        constructor(message: String?) : super(message) {
            statusCode = 500
        }

        constructor(statusCode: Int, message: String?) : super(message) {
            this.statusCode = statusCode
        }

        companion object {
            fun createFromException(ex: Exception): NetworkApiException {
                return NetworkApiException(500, ex.message)
            }

            fun createFromThrowable(throwable: Throwable?): NetworkApiException {
                return createFromException(Exception(throwable))
            }

            fun fromErrorBody(body: ResponseBody?): NetworkApiException {
                return try {
                    if (body == null) return NetworkApiException(500, "A network error occurred")
                    val errorObject = JSONObject(body.string())
                    val statusCode =
                        if (errorObject.has("statusCode")) errorObject.getInt("statusCode") else 500
                    Timber.e("Processing error status code: %s", statusCode)
                    val errorMessage: String?
                    errorMessage = try {
                        val messageArray =
                            if (errorObject.has("message")) errorObject.getJSONArray("message") else JSONArray()
                        Timber.e("Processing error body: %s", messageArray.toString())
                        val errorBuilder = StringBuilder()
                        if (messageArray.length() > 0) {
                            errorBuilder.append(messageArray.getString(0))
                        } else {
                            errorBuilder.append("A network error occurred")
                        }
                        for (i in 1 until messageArray.length()) {
                            errorBuilder.append(", ").append(messageArray.getString(i))
                        }
                        errorBuilder.toString()
                    } catch (ignored: Exception) {
                        errorObject.getString("message")
                    }
                    NetworkApiException(statusCode, errorMessage)
                } catch (jEx: JSONException) {
                    jEx.printStackTrace()
                    NetworkApiException("A network error occurred")
                } catch (jEx: IOException) {
                    jEx.printStackTrace()
                    NetworkApiException("A network error occurred")
                }
            }
        }
    }
}