package com.vfx.rightbrainstudios.smartiotapplication.presentation.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.vfx.rightbrainstudios.smartiotapplication.R
import com.vfx.rightbrainstudios.smartiotapplication.databinding.FragmentInfoBotBinding
import com.vfx.rightbrainstudios.smartiotapplication.presentation.state.State
import com.vfx.rightbrainstudios.smartiotapplication.presentation.viewmodel.SensorViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InfoBotFragment : DialogFragment() {

    private var _binding: FragmentInfoBotBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SensorViewModel by activityViewModels()
    private var animateJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoBotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Animate the dots while waiting
        animateTypingDots()

        lifecycleScope.launch {
            viewModel.requestAiSuggestion("deviceA")
        }

        lifecycleScope.launch {
            viewModel.aiResponse.collectLatest { response ->
                if (response != null) {
                    animateJob?.cancel()
                    binding.loadingDots.visibility = View.GONE
                    binding.aiResponseText.visibility = View.VISIBLE
                    simulateTyping(response.openAIData.content , response.state == State.STABLE)
                }
            }
        }

        binding.btnClose.setOnClickListener {
            animateJob?.cancel()
            dismiss().also {
                findNavController().popBackStack()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun animateTypingDots() {
        animateJob = lifecycleScope.launch {
            val dotStates = listOf("", ".", "..", "...")
            var index = 0
            while (isActive) {
                binding.loadingDots.text = dotStates[index]
                index = (index + 1) % dotStates.size
                delay(500)
            }
        }
    }

    private suspend fun simulateTyping(text: String, isBalanced: Boolean) {
        binding.aiResponseText.text = ""

        val context = requireContext()
        val highlightColor = if (isBalanced) {
            ContextCompat.getColor(context, R.color.blue)
        } else {
            ContextCompat.getColor(context, R.color.red)
        }

        val lines = text.lineSequence().toList()

        lines.forEachIndexed { index, line ->
            val spannable = SpannableString("💬 $line\n")

            if (index == 0) {
                // Apply bold, color, and larger size to the first paragraph
                spannable.setSpan(
                    ForegroundColorSpan(highlightColor),
                    0, spannable.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    StyleSpan(Typeface.BOLD),
                    0, spannable.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    RelativeSizeSpan(1.2f), // 20% larger
                    0, spannable.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            binding.aiResponseText.append(spannable)
            delay(600)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}