package ru.netology.maps.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yandex.mapkit.geometry.Point
import ru.netology.maps.dto.PlaceMark

private const val empty = 0L

class PlaceMarkViewModel(application: Application) : AndroidViewModel(application) {
    private var list = listOf<PlaceMark>()
    private var nextId: Long = 1

    private val edited = MutableLiveData(empty)

    private val _data: MutableLiveData<List<PlaceMark>> = MutableLiveData(list)
    val data: LiveData<List<PlaceMark>> = _data

    fun savePoint(placeMark: PlaceMark) {
        edited.value?.let { id ->
            println(id)
            if (id == 0L) {
                list = listOf(placeMark.copy(id = nextId++)) + list
            } else {
                list = list.map {
                    if (it.id != id) it else it.copy(name = placeMark.name)
                }
                edited.value = empty
            }
        }
        _data.value = list
//        if (placeMark.id == 0L) {
//            list = listOf(placeMark.copy(id = nextId++)) + list
//        }
//        _data.value = list
    }

    fun delete(placeMark: PlaceMark) {
        list = list.filter { it != placeMark }
        _data.value = list
    }

    fun edit(id: Long) {
        edited.value = id
    }


}