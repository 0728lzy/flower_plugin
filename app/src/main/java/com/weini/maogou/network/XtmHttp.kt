package com.weini.maogou.network



import com.weini.maogou.bean.dj.WHResponseBase
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *
 * Description:
 */
object XtmHttp {
    fun <T> toSubscribe(o: Observable<WHResponseBase<T>>, b: XtmObserver<T>, delayMILLISECONDS: Long = 0L) {
        o.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry(delayMILLISECONDS)//请求失败重连次数
            .subscribe(b as Observer<in Any>)
    }
}