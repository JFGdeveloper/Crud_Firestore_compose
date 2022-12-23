package dev.leonardom.firebasecrud.data.repositories

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObject
import dev.leonardom.firebasecrud.data.model.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    private val bookList: CollectionReference
) {

    fun addNewBook(book: Book){
        try {
            bookList.document(book.id).set(book)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }


    /**
     * recupero la lista de firestore con un flow
     * como puede ser un error o varios, creo los estados en una data para su manejo
     * en esta capa
     */
    fun getBookList(): Flow<Result<List<Book>>> = flow {
        // para emitir valor dentro de los flow usamos emit()

        try {
            // primero emito el loading
            emit(Result.Loading<List<Book>>())

            // hago la peticion a FIREBASE pero lo mapeo a un book
            val bookList = bookList.get().await().map{ document ->
                document.toObject(Book::class.java)

            }

            // ahora que ya lo tenemos la emito
            emit(Result.Success<List<Book>>(data = bookList))

        }catch (e:Exception){
            emit(Result.Error<List<Book>>(message = e.localizedMessage ?: "ERROR DESCONOCIDO "))
        }
    }


    fun getBookById(bookId:String): Flow<Result<Book>> = flow {
        try {
            // primero emito el loading
            emit(Result.Loading<Book>())

            // hago la peticion a FIREBASE pero lo mapeo a un book
            val bookList = bookList
                .whereGreaterThanOrEqualTo("id",bookId)
                .get()
                .await()
                .toObjects(Book::class.java)
                .first() // el no sabe cual elegir se lo especifico con first

            // ahora que ya lo tenemos la emito
            emit(Result.Success<Book>(data = bookList))

        }catch (e:Exception){
            emit(Result.Error<Book>(message = e.localizedMessage ?: "ERROR DESCONOCIDO "))
        }
    }


    fun updateBook(bookId:String,book: Book){
        // creo el mapa para actualizar
        try {
            val map = mapOf(
                    "title" to book.title,
                    "author" to book.author
            )

        // y ahora si se lo paso a firebase
            bookList.document(bookId).update(map)

        }catch (e: Exception){
            e.printStackTrace()
        }
    }


    fun deleteBook(bookId: String){
        try {
            bookList.document(bookId).delete()

        }catch (e: Exception){
            e.printStackTrace()

        }
    }




}