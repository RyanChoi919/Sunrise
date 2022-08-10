package com.nodes.sunrise.components.utils

import android.content.Context
import com.nodes.sunrise.BuildConfig
import com.opencsv.CSVReader
import java.io.FileReader
import java.io.IOException
import java.io.InputStreamReader

class CsvUtil(val context: Context) {

    fun readAllFromCsv(fileName: String): List<Array<String>> {
        return try {
            val inputStream = context.assets.open(fileName)
            val reader = CSVReader(InputStreamReader(inputStream))
            reader.use {
                it.readAll()
            }
        } catch (e: IOException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            listOf()
        }
    }
}