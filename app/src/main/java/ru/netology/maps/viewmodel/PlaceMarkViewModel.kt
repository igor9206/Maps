package ru.netology.maps.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.maps.dto.PlaceMark
import ru.netology.maps.repository.PlaceMarkRepository
import ru.netology.maps.repository.PlaceMarkRepositoryFileImpl

private const val empty = 0L

class PlaceMarkViewModel(application: Application) : AndroidViewModel(application) {

    private val edited = MutableLiveData(empty)
    private val repository: PlaceMarkRepository = PlaceMarkRepositoryFileImpl(application)

    val data: LiveData<List<PlaceMark>> = repository.getAll()

    fun savePoint(placeMark: PlaceMark) {
        edited.value?.let { id ->
            println(id)
            if (id == 0L) {
                repository.savePoint(placeMark)
            } else {
                repository.savePoint(placeMark.copy(id = id))
            }
            edited.value = empty
        }
    }


    fun delete(placeMark: PlaceMark) {
        repository.delete(placeMark)
    }

    fun edit(id: Long) {
        edited.value = id
    }


}