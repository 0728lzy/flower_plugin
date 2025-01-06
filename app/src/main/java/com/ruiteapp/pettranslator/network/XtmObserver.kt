package com.ruiteapp.pettranslator.network

import android.util.Log
import com.ruiteapp.pettranslator.bean.dj.ZZResponseBase
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 *
 * @author Beta-Tan
 * @date 2018/3/8
 * Description:观察者，传入泛型即可
 */
abstract class XtmObserver<T>(
) : Observer<ZZResponseBase<T>> {

    override fun onError(e: Throwable) {
        e?.printStackTrace()
        Log.i("confiInit", "Throwable=${e.message}")
    }


    override fun onNext(t: ZZResponseBase<T>) {
        Log.i("confiInit", "Response=${t.code}")

    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onComplete() {
    }

}