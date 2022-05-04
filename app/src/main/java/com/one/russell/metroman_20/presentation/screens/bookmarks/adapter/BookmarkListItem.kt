package com.one.russell.metroman_20.presentation.screens.bookmarks.adapter

import androidx.annotation.ColorInt
import com.one.russell.metroman_20.domain.Bookmark

data class BookmarkListItem(
    val id: String,
    val bpm: Int,
    val timeSignature: Int,
    val isSelected: Boolean,
    @ColorInt val colorPrimary: Int,
    @ColorInt val colorSecondary: Int
)

fun Bookmark.toListItem(
    @ColorInt colorPrimary: Int,
    @ColorInt colorSecondary: Int
): BookmarkListItem {
    return BookmarkListItem(
        id,
        bpm,
        beatTypesList.size,
        isSelected,
        colorPrimary,
        colorSecondary
    )
}