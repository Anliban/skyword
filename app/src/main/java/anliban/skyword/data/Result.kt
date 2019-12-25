package anliban.skyword.data

data class Result<out T> private constructor(
    val status: Status, val data: T?, val error: Error?
) {
    companion object {

        fun <T> success(data: T): Result<T> =
            Result(Status.SUCCESS, data, null)

        fun error(e: Exception?, code: Int): Result<Nothing> =
            Result(Status.ERROR, null, Error(e, code))

        fun error(e: Exception?): Result<Nothing> =
            Result(Status.ERROR, null, Error(e, null))
    }
}

data class Error(
    val e: Exception?,
    val code: Int?
)

enum class Status {
    SUCCESS, ERROR
}