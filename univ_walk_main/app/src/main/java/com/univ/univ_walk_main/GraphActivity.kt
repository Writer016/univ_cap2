package com.univ.univ_walk_main

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.univ.univ_walk_main.databinding.ActivityGraphBinding
import java.util.Calendar
import java.util.Date

class GraphActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGraphBinding
    lateinit var stepsArray: ArrayList<String>
    lateinit var weekStepsArray: Array<String>
    lateinit var entries: ArrayList<Entry>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGraphBinding.inflate(layoutInflater)
        setContentView(binding.root)

        entries = ArrayList() //ArrayList 인스턴스
        stepsArray = intent.getStringArrayListExtra("stepsArrayList")!!
        weekStepsArray = emptyArray()

        val calendarInfos = SimpleDateFormat("YYYY#MM#dd").format(Date(binding.graphCalendarView.date)).split("#") //선택한 날짜
        val year = calendarInfos[0].toInt()
        val month = calendarInfos[1].toInt()
        val day = calendarInfos[2].toInt()

        setGraph(getWeek(year, month, day))

        binding.graphCalendarView.setOnDateChangeListener { calendarView, i, i2, i3 ->
            entries = ArrayList()
            setGraph(getWeek(i, i2+1, i3))
        }
    }

    fun getWeek(year: Int, month: Int, day: Int): Int{
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month-1)
        calendar.set(Calendar.DATE, day)
        var max = 0
        for(i in 0..<7){ //선택된 주의 월요일 날짜 구하기 (월요일부터 일요일까지의 데이터를 출력해야 하니까)
            if(calendar.get(Calendar.DAY_OF_WEEK) == 2) break //월요일이면 현재 날짜 그대로 빠져나오기
            calendar.add(Calendar.DATE, -1)
        }
        for(i in 0..<7){ //선택된 주의 월요일부터 일요일까지의 걸음 수 그래프를 위해 걸음 수 데이터 차례대로 불러오기
            var steps = 0f //걸음 수 초기값 (데이터가 없으면 이 값이 채택됨)
            for(j in 0..<stepsArray.size){ //데이터가 저장된 모든 날짜 검사하기
                val stepsString = stepsArray.get(j) //저장된 날짜의 데이터
                val stepsInfos = stepsString.split("#")
                if(calendar.get(Calendar.YEAR) == stepsInfos[0].toInt() &&
                    calendar.get(Calendar.MONTH) == stepsInfos[1].toInt()-1 &&
                    calendar.get(Calendar.DATE) == stepsInfos[2].toInt()){ //해당 날짜에 걸음 값이 있다면
                    steps = stepsInfos[3].toFloat()
                    break
                }
            }
            if(max<steps) max = steps.toInt()
            entries.add(Entry(i.toFloat()+1, steps)) //월요일부터 일요일까지 걸음 데이터를 그래프를 위한 데이터로 추가
            calendar.add(Calendar.DATE, 1) //1일 더하기
        }
        return max
    }

    fun setGraph(max: Int){
        val dataSet = ArrayList<ILineDataSet>() //set들을 담는 역할
        var data: LineData //dataset을 담는 역할
        var graph = binding.graphGraph

        var set = LineDataSet(entries, "걸음 수") //set 생성
        set.setDrawValues(true)
        set.setDrawCircles(false)
        set.setDrawFilled(true)
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.color = getColor(R.color.purple_200)
        set.fillColor = getColor(R.color.purple_200)
        set.highLightColor = R.color.purple_200
        set.valueFormatter = DefaultValueFormatter(0)
        set.lineWidth = 4F //선 굵기
        set.valueTextSize = 15f
        set.valueTextColor = R.color.purple_500
        dataSet.add(set) //set 삽입
        data = LineData(dataSet) //set들이 존재하는 dataset을 삽입

        graph.setDrawGridBackground(false)
        graph.setBackgroundColor(Color.WHITE)
        graph.legend.isEnabled = true
        graph.legend.textSize = 20f
        graph.description.isEnabled = false

        val arrayList = ArrayList<String>()
        arrayList.add("")
        arrayList.add("월")
        arrayList.add("화")
        arrayList.add("수")
        arrayList.add("목")
        arrayList.add("금")
        arrayList.add("토")
        arrayList.add("일")

        val xAxis = graph.xAxis
        xAxis.setDrawLabels(true)
        xAxis.setDrawGridLines(false)
        xAxis.setTextSize(20f)
        xAxis.valueFormatter = IndexAxisValueFormatter(arrayList)
        xAxis.textColor = getColor(R.color.purple_500)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.axisMaximum = 7f //축 최대값
        xAxis.axisMinimum = 1f //축 최소값
        xAxis.labelCount = 12

        val yAxisR = graph.axisRight
        val yAxisL = graph.axisLeft
        yAxisR.setDrawLabels(false)
        yAxisR.setDrawGridLines(false)
        yAxisR.setDrawAxisLine(false)
        yAxisL.setDrawLabels(false)
        yAxisL.setDrawGridLines(false)
        yAxisL.setDrawAxisLine(false)
        yAxisL.axisMaximum = max.toFloat()
        yAxisL.axisMinimum = 0f

        graph.data = data
        graph.invalidate()
    }
}