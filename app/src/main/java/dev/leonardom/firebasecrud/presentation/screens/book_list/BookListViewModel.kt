package dev.leonardom.firebasecrud.presentation.screens.book_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.leonardom.firebasecrud.data.model.Book
import dev.leonardom.firebasecrud.data.repositories.BookRepository
import dev.leonardom.firebasecrud.data.repositories.Result
import dev.leonardom.firebasecrud.domain.book.BookListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(
    private val bookListRepository: BookRepository
): ViewModel() {

    private val _stateBooks = mutableStateOf(BookListState())
    val stateBooks: State<BookListState> get() = _stateBooks

    // la uso para el usuario cunado refresca la pantalla
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing


    init {
        //relleno el estado con el repo
        getBookList()
    }

    /***
     * con el repository cambio el estado que usare en la ui
     * como el repo emite un flow lo uso para llenar el estado que
     * he creado para la ui
     */

    fun getBookList(){

        // uso onEach para cada result
        bookListRepository.getBookList().onEach { result ->
            when(result){
                is Result.Error -> {
                    _stateBooks.value = BookListState(error = result.message ?: "Error desconocido")
                }
                is Result.Loading -> {
                    _stateBooks.value = BookListState(isLoading = true)
                }
                is Result.Success -> {

                    _stateBooks.value = BookListState(books = result.data ?: emptyList())
                }
            }
        }.launchIn(viewModelScope)

    }



    fun deleteBook(bookId: String){
        bookListRepository.deleteBook(bookId)
    }
}