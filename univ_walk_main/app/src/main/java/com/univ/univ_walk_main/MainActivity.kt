package com.univ.univ_walk_main

import android.Manifest
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.kotlin_mvvm.Model.Steps
import com.example.kotlin_mvvm.Repository.StepsRepository
import com.example.kotlin_mvvm.ViewModel.StepsViewModel
import com.univ.univ_walk_main.databinding.ActivityMainBinding
import com.univ.univ_walk_main.sensor.Factory.StepsFactory
import com.univ.univ_walk_main.util.LocationUtil
import com.univ.univ_walk_main.util.NetworkUtil
import com.univ.univ_walk_main.util.RetrofitUtil
import com.univ.univ_walk_main.util.SensorUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: StepsViewModel

    private lateinit var outAnim: Animation
    private lateinit var inAnim: Animation

    private var isMenuClicked = false

    lateinit var locationUtil: LocationUtil
    lateinit var networkUtil: NetworkUtil
    lateinit var retrofitUtil: RetrofitUtil
    lateinit var sensorUtil: SensorUtil

    lateinit var sensorManager: SensorManager
    var counterSensor: Sensor? = null
    var todaySteps: Steps? = null
    var steps = 0
    var firstRegister = false
    var isRegistered = false
    var isFirstRun = true //아직 첫 번째 실행인가?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repo = StepsRepository(this.application)
        viewModel = ViewModelProvider(this, StepsFactory(repo)).get(StepsViewModel::class.java)

        locationUtil = LocationUtil(this)
        networkUtil = NetworkUtil(this)
        retrofitUtil = RetrofitUtil(this)
        sensorUtil = SensorUtil(this)

        outAnim = AnimationUtils.loadAnimation(this, R.anim.fitem_out_anim) //애니메이션
        inAnim = AnimationUtils.loadAnimation(this, R.anim.fitem_in_anim)

        binding.buttonMenu.setOnClickListener { floatMenuClicked() } //오른쪽 하단 버튼 클릭 시

        if (sensorUtil.checkPermission() && !locationUtil.checkPermission()){ //미승인 시
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.ACCESS_FINE_LOCATION), 200) //request permission
        } else if (!sensorUtil.checkPermission()){
            sensorUtil.requestPermission()
        } else if (!locationUtil.checkPermission()){
            locationUtil.requestPermission(this)
        }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        counterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) //센서 가져오기
        if(counterSensor == null){ //센서를 가져올 수 없으면
            Toast.makeText(this, "센서 확인되지 않음", Toast.LENGTH_SHORT).show()
        }

        networkUtil.registerNetwork()

        var observeFlag = true
        networkUtil.networkState.observe(this, {it: Boolean -> //network 상태 변하면 작동
            if(it && observeFlag){
                observeFlag = false
                loadAndSet()
            }
        }) //호출 시, it에 적절한 값이 대입되어 리턴 됨 (SAM)
    }

    private fun registerListener(){
        sensorManager.registerListener(this, counterSensor, SensorManager.SENSOR_DELAY_FASTEST) //센서 등록
    }

    private fun floatMenuClicked(){
        isMenuClicked = !isMenuClicked
        setVisibility()
        setAnimation()
    }

    private fun setVisibility(){
        if(isMenuClicked){ binding.buttonFriend.visibility = View.VISIBLE
            binding.buttonGraph.visibility = View.VISIBLE
            binding.buttonAchievement.visibility = View.VISIBLE
        }else{ binding.buttonFriend.visibility = View.INVISIBLE
            binding.buttonGraph.visibility = View.INVISIBLE
            binding.buttonAchievement.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(){
        if(isMenuClicked){ binding.buttonFriend.startAnimation(outAnim)
            binding.buttonGraph.startAnimation(outAnim)
            binding.buttonAchievement.startAnimation(outAnim)
        } else { binding.buttonFriend.startAnimation(inAnim)
            binding.buttonGraph.startAnimation(inAnim)
            binding.buttonAchievement.startAnimation(inAnim)
        }
    }

    private fun loadAndSet(){
        if(networkUtil.networkState.value == true && locationUtil.checkPermission() && locationUtil.checkGPS()) {
            loadWeather()
        } else{ Toast.makeText(this, "네트워크나 위치 기능의 ON/OFF를 확인해주세요", Toast.LENGTH_LONG).show() }
    }

    private fun loadWeather(){

        locationUtil.getLocation() //위치 정보 불러오기
        locationUtil.locState.observe(this){
            if(it){
                lifecycleScope.launch(Dispatchers.Default) {
                    retrofitUtil.callAPI(locationUtil.getLatitude().toString(), locationUtil.getLongitude().toString()) //API
                }
            }else{ Toast.makeText(this, "위치 및 날씨 정보를 불러오는 데에 실패했습니다.", Toast.LENGTH_LONG).show() }
        }

        retrofitUtil.callState.observe(this){ //api call 결과
            if(it) { setWeatherView() //성공
            } else{ Toast.makeText(this, "날씨 정보를 불러오는 데에 실패했습니다.", Toast.LENGTH_LONG).show() } //실패
            networkUtil.unRegisterNetwork()
        }
    }

    private fun setWeatherView(){
        val title = retrofitUtil.getTitle()
        val name = retrofitUtil.getName()
        when (title) {
            "Rain" -> { binding.textWeather.text = "${name}, 오늘은 비가 옵니다 -- 우산이나 우비를 착용하시는 것은 어떨까요?" }
            "Snow" -> { binding.textWeather.text = "${name}, 오늘은 눈이 옵니다 -- 우산이나 우비를 착용하시는 것은 어떨까요?" }
            else -> { binding.textWeather.text = "${name}, 오늘의 날씨 -- ${title}" }
        }
    }

    override fun onResume(){ //! 액티비티가 다시 시작하거나 처음 시작하면
        super.onResume()
        val calendar = Calendar.getInstance()
            lifecycleScope.launch{ //센서 등록 및 초기 설정을 완료하지 않았을 시,
                if(isFirstRun) {
                    withContext(Dispatchers.IO) {//걸음 수를 가져오거나 처음 설정
                        val stepsList = viewModel.getTodaySteps(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE)).toTypedArray()
                        if (stepsList.size != 0) { //오늘 걸은 적 있다면
                            steps = stepsList.get(0).steps //오늘의 걸음수 데이터
                            todaySteps = stepsList.get(0)
                        } else { //오늘 걷는 게 처음이라면
                            val stepsV = Steps(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE), steps, 1)
                            viewModel.insert(stepsV) //오늘까지 계속 사용할 데이터 삽입
                            todaySteps = stepsV //데이터 갱신을 위해, 방금 삽입한 오늘의 데이터를 다른 메서드에 제공
                        }
                    } //초기 설정 완료
                    binding.textview.setText(steps.toString()) //걸음 수를 바탕화면에 표시
                    isFirstRun = false
                }

                if(counterSensor != null) {
                    registerListener()
                    firstRegister = true //센서 등록 완료된 직후!! (걸음 수 +1로 계산 하면 안 됨)
                    isRegistered = true //센서 등록 완료
                }
            }
    }

    override fun onPause() { //액티비티 일시정지 시,
        super.onPause()
        if(counterSensor != null && isRegistered) { //센서가 있고 센서가 등록된 상태이면
            sensorManager.unregisterListener(this) //센서 등록 해제
            isRegistered = false //센서 등록 해제
        }

        if(todaySteps != null){ //세어 둔 오늘의 걸음 수가 있다면
            todaySteps!!.steps = steps //onPause 상태일 때 마다 현재 걸음수로 갱신 및 저장
            viewModel.update(todaySteps!!)
        }
    }

    override fun onSensorChanged(event: SensorEvent){ //걸음 수 데이터 갱신
        if(counterSensor != null && !firstRegister){
            steps++ //값 변경 시마다 걸음수 올라가게
            binding.textview.setText(steps.toString())
        } else if (counterSensor != null && firstRegister){ //센서 등록 완료된 직후!!
            firstRegister = false
        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}