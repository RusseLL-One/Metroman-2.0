package com.one.russell.synthronome.presentation.views.beat_types

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.one.russell.synthronome.databinding.ListItemBeatTypeBinding

class BeatTypesAdapter(
    onBeatTypeClick: (index: Int) -> Unit
) : AsyncListDifferDelegationAdapter<BeatTypeItem>(
    DiffCallback,
    beatTypeAdapterDelegate(onBeatTypeClick)
)

fun beatTypeAdapterDelegate(
    onBeatTypeClick: (index: Int) -> Unit
) = adapterDelegateViewBinding<BeatTypeItem, BeatTypeItem, ListItemBeatTypeBinding>(
    viewBinding = { layoutInflater, root -> ListItemBeatTypeBinding.inflate(layoutInflater, root, false) },
    block = {
        binding.vBeatType.setOnClickListener { onBeatTypeClick(item.index) }
        bind { payload ->
            binding.vBeatType.setupPaints(item.colorPrimary, item.colorSecondary)
            if (Payload.ANIMATE_BEAT_TYPE_CHANGE in payload) {
                binding.vBeatType.setBeatType(item.beatType, animate = true)
            } else {
                binding.vBeatType.setBeatType(item.beatType, animate = false)
            }
        }
    }
)

enum class Payload {
    ANIMATE_BEAT_TYPE_CHANGE
}