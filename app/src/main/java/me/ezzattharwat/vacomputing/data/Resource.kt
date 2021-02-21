package me.ezzattharwat.vacomputing.data

// A generic class that contains data and status about loading this data.
sealed class Resource<T>(
        val data: T? = null,
        val errorMassage: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class DataError<T>(e: String) : Resource<T>(null, e)
}
