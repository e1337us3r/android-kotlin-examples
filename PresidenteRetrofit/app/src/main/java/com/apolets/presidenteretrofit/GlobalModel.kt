package com.apolets.presidenteretrofit

object GlobalModel {
    val presidents: kotlin.collections.MutableList<President> = java.util.ArrayList()

    init {

// construct the data source
        presidents.add(President("Kaarlo Stahlberg", 1919, 1925, "Eka presidentti"))
        presidents.add(President("Testing", 2020, 2023, "Future president"))
        presidents.add(President("dsd", 1925, 1931, "Toka presidentti"))
        presidents.add(President("Kaarlo Stahlberg", 1919, 1925, "Eka presidentti"))
        presidents.add(President("Testing", 2020, 2023, "Future president"))
        presidents.add(President("dsd", 1925, 1931, "Toka presidentti"))
        presidents.sort()
    }
}