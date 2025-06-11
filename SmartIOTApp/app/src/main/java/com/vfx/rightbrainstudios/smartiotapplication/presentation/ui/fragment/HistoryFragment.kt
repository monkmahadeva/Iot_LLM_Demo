package com.vfx.rightbrainstudios.smartiotapplication.presentation.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vfx.rightbrainstudios.smartiotapplication.R
import com.vfx.rightbrainstudios.smartiotapplication.adapter.SensorHistoryAdapter
import com.vfx.rightbrainstudios.smartiotapplication.presentation.viewmodel.SensorViewModel
import kotlinx.coroutines.launch

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private val viewModel: SensorViewModel by activityViewModels()
    private lateinit var adapter: SensorHistoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SensorHistoryAdapter()
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerHistory)
        val textEmpty = view.findViewById<TextView>(R.id.textEmpty)
        val progress = view.findViewById<ProgressBar>(R.id.progressBar)

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.historyData.collect {
                    if (it.isEmpty()) {
                        textEmpty.visibility = View.VISIBLE
                        recycler.visibility = View.GONE
                    } else {
                        textEmpty.visibility = View.GONE
                        recycler.visibility = View.VISIBLE
                        adapter.submitList(it)
                    }
                }
            }
        }
    }
}