package com.one.russell.metroman_20.presentation.screens.bookmarks.adapter

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.databinding.ListItemBookmarkBinding

class BookmarksAdapter(
    onBookmarkClicked: (id: String) -> Unit,
    onRemoveClicked: (id: String) -> Unit,
) : AsyncListDifferDelegationAdapter<BookmarkListItem>(
    DiffCallback,
    bookmarkAdapterDelegate(onBookmarkClicked, onRemoveClicked)
)

fun bookmarkAdapterDelegate(
    onBookmarkClicked: (id: String) -> Unit,
    onRemoveClicked: (id: String) -> Unit,
) = adapterDelegateViewBinding<BookmarkListItem, BookmarkListItem, ListItemBookmarkBinding>(
    viewBinding = { layoutInflater, root ->
        ListItemBookmarkBinding.inflate(layoutInflater, root, false)
    },
    block = {
        binding.vBookmark.setOnClickListener { onBookmarkClicked(item.id) }
        binding.vBookmark.setOnRemoveButtonClickListener { onRemoveClicked(item.id) }
        bind { payload ->
            if (Payload.BOOKMARK_SELECTED in payload) {
                binding.vBookmark.setToggled(item.isSelected, animate = true)
                return@bind
            }
            binding.tvBpm.text = context.getString(R.string.main_bpm, item.bpm)
            binding.tvTimeSignature.text = context.getString(R.string.main_time_signature, item.timeSignature)
            binding.vBookmark.setupPaints(item.colorPrimary, item.colorSecondary)
            binding.vBookmark.setToggled(item.isSelected, animate = false)
        }
    }
)

enum class Payload {
    BOOKMARK_SELECTED
}
