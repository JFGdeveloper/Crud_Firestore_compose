package dev.leonardom.firebasecrud.presentation.screens.book_detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.leonardom.firebasecrud.data.model.Book
import dev.leonardom.firebasecrud.domain.book.BookDetailState
import dev.leonardom.firebasecrud.presentation.ui.theme.Red100

@Composable
fun BookDetailScreen(
    state: BookDetailState,
    addNewBook: (String,String)->Unit,
    updateBook:(String,String)->Unit
) {

    var title by remember(state.book?.title) { mutableStateOf(state.book?.title ?: "") }
    var author by remember(state.book?.author) { mutableStateOf(state.book?.author ?: "") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                value = title,
                onValueChange = { title = it},
                label = {
                    Text(text = "Title")
                }
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                value = author,
                onValueChange = { author = it},
                label = {
                    Text(text = "Author")
                }
            )
        }


        // MUESTRA UN CIRCULAR O EL BOTON SI NO ESTA CARGANDO
        if (state.isLoading){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }else{
            // MOSTRAMOS EL BOTON DEPENDIENDO DE SI ES NULO
            if (state.book?.id != null){
                Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        onClick = {
                            updateBook(title,author)
                        },
                        colors = ButtonDefaults.buttonColors(
                                backgroundColor = Red100
                        )
                ) {
                    Text(
                            text = "Update Book",
                            color = Color.White
                    )
                }


            }else{
                Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        onClick = {
                            addNewBook(title,author)
                        },
                        colors = ButtonDefaults.buttonColors(
                                backgroundColor = Red100
                        )
                ) {
                    Text(
                            text = "Add New Book",
                            color = Color.White
                    )
                }

            }
        }

        // SI HAY UN ERROR
        if (state.error.isBlank()){
            Text(text = state.error, color = Color.Red, modifier = Modifier.align(Alignment.Center))
        }
    }
}