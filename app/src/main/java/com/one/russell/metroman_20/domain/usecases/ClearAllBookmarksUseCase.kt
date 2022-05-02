package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.domain.providers.BookmarksProvider

class ClearAllBookmarksUseCase(
    private val bookmarksProvider: BookmarksProvider
) {
    fun execute() {
        bookmarksProvider.changeValue { emptyList() }
    }
}