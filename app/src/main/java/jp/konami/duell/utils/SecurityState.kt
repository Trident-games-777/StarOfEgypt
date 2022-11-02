package jp.konami.duell.utils

enum class SecurityState(private val state: String?) {
    ADMIN("1"), USER("0");

    companion object {
        fun from(value: String): SecurityState =
            values().find { it.state == value }
                ?: throw IllegalArgumentException("Wrong security value")

        fun from(value: Boolean): SecurityState = if (value) ADMIN else USER
    }
}