package com.nammaraste.app.ui

sealed class Screen(val route: String) {
    object Directory : Screen("directory")
    object Report : Screen("report")
    object ContractorInfo : Screen("contractorInfo")
    object Map : Screen("map")
}
