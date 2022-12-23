package dev.leonardom.firebasecrud.domain.book

import dev.leonardom.firebasecrud.data.model.Book

data class BookListState(
    val isLoading: Boolean= false,
    val books: List<Book> = emptyList(),
    val error: String = ""
)
