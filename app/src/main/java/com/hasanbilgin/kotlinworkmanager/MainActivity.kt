package com.hasanbilgin.kotlinworkmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ResultReceiver
import androidx.lifecycle.Observer
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Data work olcak
        val data = Data.Builder().putInt("intKey", 1).build()
        //Constraints work seçildi
        //NetworkType.CONNECTED  ağa bağlı olsun
        //NetworkType.NOT_ROAMING yurt dışında çalışıyor olmasın
        //NetworkType.NOT_REQUIRED ağ önemli değil
        //setRequiresChargins telefon şarzz oluyormju olmuyoumu ona göre
        //setRequiresBatteryNotLow pilin az olmasını istemiyorum iş için
        var constrains = Constraints.Builder()
            //.setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false).build()

        //OneTimeWorkRequestBuilder<>() birkez çalıştırır
//        val myWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<RefreshDatabase>().setConstraints(constrains).setInputData(data)
//            //.setInitialDelay(5,TimeUnit.MINUTES) 5dakika sonra bu işlemi başlat
//            //.setInitialDelay(5,TimeUnit.MINUTES)
//            //tag atanması
//            //.addTag("myTag")
//            .build()
//        //enqueue workRequest kullanıldı
        //çalıştırmak
//        WorkManager.getInstance(this).enqueue(myWorkRequest)

        //PeriodicWorkRequestBuilder<>() belli aralıklarla çalıştırır
        //(15, TimeUnit.MINUTES) 15 dakikada bir çalışçaktır en az 15dakkadır
        val myWorkRequest: PeriodicWorkRequest = PeriodicWorkRequestBuilder<RefreshDatabase>(15, TimeUnit.MINUTES).setConstraints(constrains).setInputData(data).build()
        WorkManager.getInstance(this).enqueue(myWorkRequest)

        //tabi getWorkInfosByTagLiveData tag ilede alınabilir
        //observe gözlem yapma olanağı
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(myWorkRequest.id).observe(this, Observer {
            //Running çalışıyor
            //failed başarız olması
            //succeed ed başarılı olması
            //succeeded  başarılı olması
            //blocked bloklanması
            //cancelled iptal edilmesi
            //enqueued sıraya alınması
            if (it.state == WorkInfo.State.RUNNING) {
                println("RUNNING")
            } else if (it.state == WorkInfo.State.FAILED) {
                println("FAILED")
            } else if (it.state == WorkInfo.State.SUCCEEDED) {
                println("SUCCEEDED")
            }
        })

        //workun iptal edilmesi
        //cancelAllWork() bütün workleri iptal eder
        //cancelAllWorkByTag() taga göre iptal eder
        //cancelWorkById() id göre iptal eder
        //WorkManager.getInstance(this).cancelAllWork()

        //Chaining -arka arkaya zincirleme bunları bağlamak
        //sadece onatime resuestler yapılabiliyor
        //periota olmaz
        //zincire girebilmesi için OneTimeWorkRequest olması şart
        //aynı request 1 den fazla zincire girebilir
        val oneTimeRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<RefreshDatabase>().setConstraints(constrains).setInputData(data).build()

        WorkManager.getInstance(this).beginWith(oneTimeRequest).then(oneTimeRequest).then(oneTimeRequest).enqueue()


    }
}