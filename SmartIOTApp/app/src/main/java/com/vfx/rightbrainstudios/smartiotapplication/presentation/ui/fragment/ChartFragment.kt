package com.vfx.rightbrainstudios.smartiotapplication.presentation.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.vfx.rightbrainstudios.smartiotapplication.R
import com.vfx.rightbrainstudios.smartiotapplication.databinding.FragmentChartBinding
import com.vfx.rightbrainstudios.smartiotapplication.presentation.viewmodel.SensorViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.collections.mapIndexed

@AndroidEntryPoint
class ChartFragment: Fragment() {
    var _binding: FragmentChartBinding? = null
    val binding
        get() = _binding

    private var dataJob: Job? = null

    val viewModel: SensorViewModel by activityViewModels<SensorViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val chart = binding!!.lineChartTemp

        chart.description.isEnabled = false
        chart.setTouchEnabled(true)
        chart.setPinchZoom(true)
        chart.legend.isEnabled = true

        val tempDataSet = LineDataSet(mutableListOf(), "Temperature (°C)").apply {
            color = Color.RED
            valueTextColor = Color.BLACK
            lineWidth = 2f
            setDrawCircles(false)
        }

        val humidityDataSet = LineDataSet(mutableListOf(), "Humidity (%)").apply {
            color = Color.rgb(100, 149, 237)
            valueTextColor = Color.BLACK
            lineWidth = 2f
            setDrawCircles(false)
        }

        val lineData = LineData(tempDataSet, humidityDataSet)
        chart.data = lineData
        chart.invalidate()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                startCollectingChartData(tempDataSet, humidityDataSet, lineData)
            }
        }

        binding!!.btnGoToAnalysis.setOnClickListener {
            findNavController().navigate(R.id.action_chartFragment_to_infoBotFragment)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun startCollectingChartData(
        tempDataSet: LineDataSet,
        humidityDataSet: LineDataSet,
        lineData: LineData
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.liveChartData.collect { sensorList ->
                    val tempEntries = sensorList.mapIndexed { i, sensor ->
                        Entry(i.toFloat(), sensor.temperature.toFloat())
                    }

                    val humidityEntries = sensorList.mapIndexed { i, sensor ->
                        Entry(i.toFloat(), sensor.humidity.toFloat())
                    }

                    tempDataSet.values = tempEntries
                    humidityDataSet.values = humidityEntries

                    lineData.notifyDataChanged()
                    binding?.lineChartTemp?.apply {
                        notifyDataSetChanged()
                        invalidate()
                    }

                    sensorList.lastOrNull()?.let {
                        binding?.textTemp?.text =
                            "🌡️ Temp: ${it.temperature}°C   💧 Humidity: ${it.humidity}%"
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dataJob?.cancel()
        _binding = null
    }
}

