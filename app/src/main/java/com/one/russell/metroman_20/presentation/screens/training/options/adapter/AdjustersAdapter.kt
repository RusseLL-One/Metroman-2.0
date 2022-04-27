package com.one.russell.metroman_20.presentation.screens.training.options.adapter

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.databinding.ListItemKnobBinding
import com.one.russell.metroman_20.databinding.ListItemPickerBinding
import com.one.russell.metroman_20.presentation.screens.training.options.ControlType
import com.one.russell.metroman_20.presentation.screens.training.options.OptionsAdjusterType

class AdjustersAdapter(
    onValueChanged: (type: OptionsAdjusterType, value: Int) -> Unit
) : AsyncListDifferDelegationAdapter<AdjusterListItem>(
    DiffCallback,
    knobAdapterDelegate(onValueChanged),
    pickerAdapterDelegate(onValueChanged)
)

fun knobAdapterDelegate(
    onValueChanged: (type: OptionsAdjusterType, value: Int) -> Unit
) = adapterDelegateViewBinding<AdjusterListItem, AdjusterListItem, ListItemKnobBinding>(
    viewBinding = { layoutInflater, root ->
        ListItemKnobBinding.inflate(layoutInflater, root, false)
    },
    on = { item, _, _ -> item.type.controlType == ControlType.KNOB },
    block = {
        binding.vKnob.addOnValueChangedCallback {
            onValueChanged(item.type, (item.value + it)
                .coerceIn(item.type.minValue, item.type.maxValue))
        }
        bind { payload ->
            if (payload.isEmpty() || payload[0] != Payload.BPM_CHANGED) {
                binding.tvTitle.text = binding.root.context.getString(item.type.titleRes)
            }
            binding.tvValue.text = binding.root.context.getString(R.string.main_bpm, item.value)
            binding.vKnob.setGlowIntense(item.value)
        }
    }
)

fun pickerAdapterDelegate(
    onValueChanged: (type: OptionsAdjusterType, value: Int) -> Unit
) = adapterDelegateViewBinding<AdjusterListItem, AdjusterListItem, ListItemPickerBinding>(
    viewBinding = { layoutInflater, root ->
        ListItemPickerBinding.inflate(layoutInflater, root, false)
    },
    on = { item, _, _ -> item.type.controlType == ControlType.PICKER },
    block = {
        binding.npPicker.setOnValueChangedListener { _, _, newVal ->
            onValueChanged(item.type, newVal * item.type.step + item.type.minValue)
        }
        bind {
            binding.tvTitle.text = binding.root.context.getString(item.type.titleRes)

            val minValue = 0
            val maxValue = (item.type.maxValue - item.type.minValue) / item.type.step
            val initValue = (item.value - item.type.minValue) / item.type.step

            binding.npPicker.wrapSelectorWheel = false
            binding.npPicker.minValue = minValue
            binding.npPicker.maxValue = maxValue
            binding.npPicker.value = initValue
            binding.npPicker.displayedValues = IntRange(item.type.minValue, item.type.maxValue)
                .step(item.type.step)
                .map { it.toString() }
                .toTypedArray()
        }
    }
)

enum class Payload {
    BPM_CHANGED
}