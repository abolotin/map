package ru.netology.nmedia.entity

data class GeoPointViewModelState(
    val status : Status = Status.READY,
    val errorMessage : String = ""
) {
    enum class Status {
        PROCESSING,
        READY,
        SAVE_ERROR,
        SAVE_OK
    }

    val isProcessing get() = status == Status.PROCESSING
    val isSaveError get() = status == Status.SAVE_ERROR
    val isSaveOk get() = status == Status.SAVE_OK
}

