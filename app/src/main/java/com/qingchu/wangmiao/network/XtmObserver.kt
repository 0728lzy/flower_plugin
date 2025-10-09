package com.qingchu.wangmiao.network

import android.util.Log
import com.qingchu.wangmiao.bean.dj.QCResponseBase
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 *
 * @author Beta-Tan
 * @date 2018/3/8
 * Description:观察者，传入泛型即可
 */
abstract class XtmObserver<T>(
) : Observer<QCResponseBase<T>> {

    override fun onError(e: Throwable) {
        e?.printStackTrace()
        Log.i("confiInit", "Throwable=${e.message}")
    }


    override fun onNext(t: QCResponseBase<T>) {
        Log.i("confiInit", "Response=${t.code}")

    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onComplete() {
    }

}