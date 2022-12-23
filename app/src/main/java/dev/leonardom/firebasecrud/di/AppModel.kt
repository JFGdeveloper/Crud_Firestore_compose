package dev.leonardom.firebasecrud.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModel {

    @Provides
    @Singleton
    fun providerFiresToreInstant()= FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun providerListBook(firestore: FirebaseFirestore)= firestore.collection("books")
}