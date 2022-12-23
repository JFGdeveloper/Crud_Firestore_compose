package dev.leonardom.firebasecrud.presentation.screens.book_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.leonardom.firebasecrud.data.model.Book
import dev.leonardom.firebasecrud.data.repositories.BookRepository
import dev.leonardom.firebasecrud.data.repositories.Result
import dev.leonardom.firebasecrud.domain.book.BookDetailState
import dev.leonardom.firebasecrud.domain.book.BookListState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val repository: BookRepository,
    savedStateHandle: SavedStateHandle // para obtener el parametro de la navigation
): ViewModel() {

    private val _state = mutableStateOf(BookDetailState())
    val state:State<BookDetailState> get() = _state

    init {
        // como puede ser nulo lo gestiono con let
        savedStateHandle.get<String>("bookId")?.let { bookId->
          getById(bookId)

        }
    }

    fun addNewBook(tittle: String,author:String){
        val book = Book(
                id = UUID.randomUUID().toString(),
                title = tittle,
                author = author,
                url = "https://picsum.photos/id/1/200/300",
                download = 0,
                ratin = 0.0f
        )

        repository.addNewBook(book)
    }


    /***
     * Igual que en el otro viewModel gestiono las repuestas del repositori
     */
    private fun getById(bookId: String){
        repository.getBookById(bookId).onEach { result->

            when(result){
                is Result.Error -> {
                    _state.value = BookDetailState(error = result.message ?: "Error desconocido")
                }
                is Result.Loading -> {
                    _state.value = BookDetailState(isLoading = true)
                }
                is Result.Success -> {

                    _state.value = BookDetailState(book = result.data)
                }
            }
        }.launchIn(viewModelScope)
    }


    fun updateBook(newTitle: String,newAuthor: String){
        if (_state.value.book?.id == null){
            _state.value = BookDetailState(error =  "Book is null")
            return

        }

        val objBook = _state.value.book!!.copy(
                title = newTitle,
                author = newAuthor

        )

        repository.updateBook(objBook.id,objBook)
    }




}