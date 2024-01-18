package ru.netology.maps.repository

import androidx.lifecycle.LiveData
import ru.netology.maps.dto.PlaceMark

interface PlaceMarkRepository {
    fun getAll(): LiveData<List<PlaceMark>>
    fun savePoint(placeMark: PlaceMark)
    fun delete(placeMark: PlaceMark)
}