package anliban.skyword.data

data class Resource<out T> private constructor(
    val status: Status, val data: T?, val error: Error?
) {
    companion object {

        fun <T> success(data: T): Resource<T> = Resource(Status.SUCCESS, data, null)

        fun error(e: Exception?, code: Int): Resource<Nothing> =
            Resource(Status.ERROR, null, Error(e, code))

        fun error(e: Exception?): Resource<Nothing> =
            Resource(Status.ERROR, null, Error(e, null))
    }
}

data class Error(
    val e: Exception?,
    val code: Int?
)

enum class Status {
    SUCCESS, ERROR
}