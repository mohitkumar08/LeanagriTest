package com.leanagritest

enum class ApiCallStatus {
     SUCCESS, FAILED, NOT_FOUND
}
enum class SortBased{
     RATING_LOW_TO__HIGH,
     RATING_HIGH_TO_LOW,
     DATE_MIN_TO_MAX,
     DATE_MAX_TO_MIN,
     POPULARITY_LOW_TO_HIGH,
     POPULARITY_HIGH_TO_LOW,
}