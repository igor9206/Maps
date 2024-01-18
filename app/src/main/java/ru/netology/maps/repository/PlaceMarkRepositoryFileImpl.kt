package ru.netology.maps.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.maps.dto.PlaceMark

class PlaceMarkRepositoryFileImpl(
    private val context: Context
) : PlaceMarkRepository {
    private val gson = Gson()
    private val placeMarkFile = "placeMark.json"
    private val nextIdFile = "next_id.json"
    private val type = TypeToken.getParameterized(List::class.java, PlaceMark::class.java).type

    private var nextId: Long = 1
    private var placeMarks = listOf<PlaceMark>()
    private val _data: MutableLiveData<List<PlaceMark>> = MutableLiveData(placeMarks)

    init {
        val placeMarksFile = context.filesDir.resolve(placeMarkFile)

        if (placeMarksFile.exists()) {
            placeMarksFile.reader().buffered().use {
                placeMarks = gson.fromJson(it, type)
                _data.value = placeMarks
            }
        } else {
            emptyList<PlaceMark>()
        }

        val nextIdFile = context.filesDir.resolve(nextIdFile)
        nextId = if (nextIdFile.exists()) {
            nextIdFile.reader().buffered().use {
                gson.fromJson(it, Long::class.java)
            }
        } else {
            nextId
        }
    }


    override fun getAll(): LiveData<List<PlaceMark>> {
        return _data
    }

    override fun savePoint(placeMark: PlaceMark) {
        placeMarks = if (placeMark.id == 0L) {
            listOf(placeMark.copy(id = nextId++)) + placeMarks
        } else {
            placeMarks.map {
                if (it.id != placeMark.id) it else it.copy(name = placeMark.name)
            }
        }
        _data.value = placeMarks
        sync()
    }

    override fun delete(placeMark: PlaceMark) {
        placeMarks = placeMarks.filter { it != placeMark }
        _data.value = placeMarks
        sync()
    }

    private fun sync() {
        context.filesDir.resolve(placeMarkFile).writer().buffered().use {
            it.write(gson.toJson(placeMarks))
        }
        context.filesDir.resolve(nextIdFile).writer().buffered().use {
            it.write(gson.toJson(nextId))
        }
    }


}