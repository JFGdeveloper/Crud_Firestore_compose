package dev.leonardom.firebasecrud.domain.book

import dev.leonardom.firebasecrud.data.model.Book

data class BookDetailState(
    val isLoading: Boolean= false,
    val book: Book? = null,
    val error: String = ""
)
