package com.univ.univ_walk_main.util
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.univ.univ_walk_main.R
import com.univ.univ_walk_main.weather.RetrofitFactory
import com.univ.univ_walk_main.weather.RetrofitService
import com.univ.univ_walk_main.weather.Model.WeatherModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitUtil(val context: Context) {
    private var responseTitle: String = ""
    private var responseName: String = ""
    private val thisCallState: MutableLiveData<Boolean> = MutableLiveData()
    val callState: LiveData<Boolean>
        get() = thisCallState

    fun getTitle() = responseTitle
    fun getName() = responseName

    fun callAPI(latitude: String, longitude: String){

        val networkService: RetrofitService = RetrofitFactory().create()
        networkService.getWeather(latitude, longitude, context.getString(R.string.appid))
                .enqueue(
                    object : Callback<WeatherModel> {
                        override fun onResponse(
                            call: Call<WeatherModel>, //모델 클래스 (DTO)
                            response: Response<WeatherModel>
                        ) {
                            if (response.isSuccessful) {
                                val responseBody = response.body()
                                responseTitle = responseBody!!.weather[0].main
                                responseName = responseBody.name
                                thisCallState.postValue(true)
                            }
                        }
                        override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                            thisCallState.postValue(false)
                        }
                    }
                )
    }
}