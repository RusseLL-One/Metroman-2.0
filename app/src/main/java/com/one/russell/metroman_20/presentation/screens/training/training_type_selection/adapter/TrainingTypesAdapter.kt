package com.one.russell.metroman_20.presentation.screens.training.training_type_selection.adapter

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.one.russell.metroman_20.databinding.ItemTrainingTypeBinding

class TrainingTypesAdapter(
    onClick: (TrainingTypeItem) -> Unit
) : AsyncListDifferDelegationAdapter<TrainingTypeItem>(
    DiffCallback,
    trainingTypeAdapterDelegate(onClick)
)

fun trainingTypeAdapterDelegate(
    onClick: (TrainingTypeItem) -> Unit
) = adapterDelegateViewBinding<TrainingTypeItem, TrainingTypeItem, ItemTrainingTypeBinding>(
    viewBinding = { layoutInflater, root ->
        ItemTrainingTypeBinding.inflate(layoutInflater, root, false)
    },
    block = {
        itemView.setOnClickListener { onClick(item) }
        bind {
            binding.tvText.text = context.getString(item.textRes)
        }
    }
)