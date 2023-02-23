package com.example.beerwager.domain.models

enum class WagerFilter {
    CLOSED,
    UPCOMING,
    FUTURE;

    override fun toString(): String {
        return when (this) {
            CLOSED -> "Closed wagers"
            UPCOMING -> "Upcoming wagers"
            FUTURE -> "Future wagers"
        }
    }
}