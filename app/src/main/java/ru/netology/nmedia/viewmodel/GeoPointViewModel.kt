package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.netology.nmedia.dto.GeoPoint
import ru.netology.nmedia.entity.GeoPointViewModelState
import ru.netology.nmedia.repository.GeoPointRepository
import javax.inject.Inject

@HiltViewModel
class GeoPointViewModel @Inject constructor(
    private val repository: GeoPointRepository
) : ViewModel() {
    private val _state = MutableLiveData<GeoPointViewModelState>()
    val state: LiveData<GeoPointViewModelState>
        get() = _state

    val data: LiveData<List<GeoPoint>> = repository.data
        .asLiveData(Dispatchers.Default)

    val targetGeoPoint = MutableLiveData<GeoPoint?>()

    var editedPost : GeoPoint = GeoPoint(0.0, 0.0)
        set(value) {
            _state.postValue(GeoPointViewModelState(status = GeoPointViewModelState.Status.READY))
            field = value
        }

    fun resetState() {
        _state.postValue(GeoPointViewModelState(status = GeoPointViewModelState.Status.READY))
    }

    fun addGeoPoint(geoPoint: GeoPoint) {
        viewModelScope.launch {
            _state.postValue(GeoPointViewModelState(status = GeoPointViewModelState.Status.PROCESSING))
            try {
                repository.save(geoPoint)
                _state.postValue(GeoPointViewModelState(status = GeoPointViewModelState.Status.SAVE_OK))
            } catch (e : Exception) {
                _state.postValue(GeoPointViewModelState(
                    status = GeoPointViewModelState.Status.SAVE_ERROR,
                    errorMessage = e.message ?: "Неизвестная ошибка"
                ))
            }
        }
    }

    fun removeGeoPoint(geoPoint: GeoPoint) {
        viewModelScope.launch {
            _state.postValue(GeoPointViewModelState(status = GeoPointViewModelState.Status.PROCESSING))
            try {
                repository.remove(geoPoint)
                _state.postValue(GeoPointViewModelState(status = GeoPointViewModelState.Status.SAVE_OK))
            } catch (e : Exception) {
                _state.postValue(GeoPointViewModelState(
                    status = GeoPointViewModelState.Status.SAVE_ERROR,
                    errorMessage = e.message ?: "Неизвестная ошибка"
                ))
            }
        }
    }
}