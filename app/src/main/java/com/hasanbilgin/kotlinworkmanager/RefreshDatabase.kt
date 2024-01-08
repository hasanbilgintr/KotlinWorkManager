package com.hasanbilgin.kotlinworkmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class RefreshDatabase(val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        //inputData almak
        val getData = inputData
        val myNumber = getData.getInt("intKey",0)
        refreshDatabase(myNumber);
        return Result.success()
    }

    private fun refreshDatabase(myNumber:Int) {
        //sınıf içinde Context böle getirebiliriz....
        val sharedPreferences = context.getSharedPreferences("com.hasanbilgin.kotlinworkmanager", Context.MODE_PRIVATE);
        var mySavedNumber = sharedPreferences.getInt("myNumber", 0)
        mySavedNumber = mySavedNumber + myNumber;
        println(mySavedNumber)
        sharedPreferences.edit().putInt("myNumber", mySavedNumber).apply()
    }
}