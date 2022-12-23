package dev.leonardom.firebasecrud.data.model

data class Book(
    val id: String,
    val title: String ,
    val author: String ,
    val url: String ,
    val download: Int,
    val ratin: Float
){
    constructor (): this("","","","",0,0.0f)

}
