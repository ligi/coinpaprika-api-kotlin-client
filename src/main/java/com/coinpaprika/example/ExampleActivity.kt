package com.coinpaprika.example

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.util.Log.i
import com.coinpaprika.apiclient.GraphPeriods
import com.coinpaprika.apiclient.api.CoinpaprikaAPI
import com.coinpaprika.apiclient.api.GraphsAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ExampleActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.makeCallToAPI()
    }

    private fun makeCallToAPI() {
        // Coinpaprika API call
        val disposable = CoinpaprikaAPI(this)
            .tickers()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { error -> error.printStackTrace() }
            .subscribe(
                { next -> for (ticker in next) {
                    i("ExampleActivity", "Ticker name is ${ticker.name} ")
                }},
                { error -> error.printStackTrace() })

        // Graphs API call
        val disposable2 = GraphsAPI(this)
            .chartSvg("btc-bitcoin", GraphPeriods.DAILY.period)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { next -> Log.i("ExampleActivity", "Example SVG file: $next") },
                { error -> error.printStackTrace() })
    }
}