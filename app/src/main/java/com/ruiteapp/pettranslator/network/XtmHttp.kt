package com.ruiteapp.pettranslator.network



import com.ruiteapp.pettranslator.bean.dj.ZZResponseBase
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *
 * Description:
 */
object XtmHttp {
    fun <T> toSubscribe(o: Observable<ZZResponseBase<T>>, b: XtmObserver<T>, delayMILLISECONDS: Long = 0L) {
        o.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry(delayMILLISECONDS)//请求失败重连次数
            .subscribe(b as Observer<in Any>)
    }
}