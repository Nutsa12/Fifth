package com.example.task5.helper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.task5.models.StatisticContract
import java.lang.Exception

class StatisticDb (context: Context): SQLiteOpenHelper(context, "NutsaDb", null, 1) {

    companion object {
        const val SQL_CREATE_TABLE = "create table ${StatisticContract.TABLE_NAME} (" +
                "${StatisticContract.StatisticColumns.ID} integer primary key, " +
                "${StatisticContract.StatisticColumns.RUN_DISTANCE} real, " +
                "${StatisticContract.StatisticColumns.SWIM_DISTANCE} real, " +
                "${StatisticContract.StatisticColumns.CALORIES} real)"

        const val SQL_DROP_TABLE = "drop table if exists ${StatisticContract.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DROP_TABLE)
        onCreate(db)
    }

    fun insertStatistic(id: Int, runDistance: Double, swimDistance: Double, calories: Double) {
        val cv = ContentValues().apply {
            put(StatisticContract.StatisticColumns.ID, id)
            put(StatisticContract.StatisticColumns.RUN_DISTANCE, runDistance)
            put(StatisticContract.StatisticColumns.SWIM_DISTANCE, swimDistance)
            put(StatisticContract.StatisticColumns.CALORIES, calories)
        }

        try {
            writableDatabase.insert(StatisticContract.TABLE_NAME, null, cv)
        } catch (exception: Exception) {
            var x = exception
            val y = 2
        }
    }

    fun updateStatistic(id: Int, run: Double, swim: Double, cal: Double) {
        val cv = ContentValues().apply {
            put(StatisticContract.StatisticColumns.RUN_DISTANCE, run)
            put(StatisticContract.StatisticColumns.SWIM_DISTANCE, swim)
            put(StatisticContract.StatisticColumns.CALORIES, cal)
        }

        val where = "${StatisticContract.StatisticColumns.ID} = ?"
        val args = arrayOf(id.toString())

        writableDatabase.update(StatisticContract.TABLE_NAME, cv, where, args)
    }

    fun deleteStatistic(id: Int) {
        val where = "${StatisticContract.StatisticColumns.ID} = ?"
        val args = arrayOf(id.toString())

        writableDatabase.delete(StatisticContract.TABLE_NAME, where, args)
    }

    @SuppressLint("Range")
    fun selectAll() {
        val columns = arrayOf(
            StatisticContract.StatisticColumns.RUN_DISTANCE,
            StatisticContract.StatisticColumns.SWIM_DISTANCE,
            StatisticContract.StatisticColumns.CALORIES
        )

        val cursor = readableDatabase.query(
            StatisticContract.TABLE_NAME, columns, null, null,
            null, null, null
        )

        while (cursor.moveToNext()) {
            val runDistance = cursor
                .getDouble(cursor.getColumnIndex(StatisticContract.StatisticColumns.RUN_DISTANCE))

            Log.d("zzz", "runDistance: ${runDistance}")

            val swimDistance = cursor
                .getDouble(cursor.getColumnIndex(StatisticContract.StatisticColumns.SWIM_DISTANCE))

            Log.d("zzz", "swimDistance: ${swimDistance}")

            val calories = cursor
                .getDouble(cursor.getColumnIndex(StatisticContract.StatisticColumns.CALORIES))   // return data if necessary

            Log.d("zzz", "calories: ${calories}")
        }

        cursor.close()
    }

    @SuppressLint("Range")
    fun getLatestId(): Int {
        val cursor =
            readableDatabase.rawQuery("select max(Id) from ${StatisticContract.TABLE_NAME}", null)

        cursor.moveToNext()
        val id = cursor.getInt(0)
        cursor.close()

        return id
    }

    fun getAvgRun(): Double {
        val cursor = readableDatabase.rawQuery(
            "select round(avg(RunDistance), 2) as 'avg' from ${StatisticContract.TABLE_NAME}",
            null
        )

        if (!cursor.moveToNext())
            return 0.0

        val avg = cursor.getDouble(0)
        cursor.close()
        return avg
    }

    fun getAvgSwim(): Double {
        val cursor = readableDatabase.rawQuery(
            "select round(avg(SwimDistance), 2) as 'avg' from ${StatisticContract.TABLE_NAME}",
            null
        )

        cursor.moveToNext()

        val avg = cursor.getDouble(0)
        cursor.close()
        return avg
    }

    fun getAvgCal(): Double {
        val cursor = readableDatabase.rawQuery(
            "select round(avg(Calories), 2) as 'avg' from ${StatisticContract.TABLE_NAME}",
            null
        )

        cursor.moveToNext()

        val avg = cursor.getDouble(0)
        cursor.close()
        return avg
    }

    fun getTotalRun(): Double {
        val cursor = readableDatabase.rawQuery(
            "select round(sum(RunDistance), 2) as 'total' from ${StatisticContract.TABLE_NAME}",
            null
        )

        cursor.moveToNext()

        val avg = cursor.getDouble(0)
        cursor.close()
        return avg
    }
}