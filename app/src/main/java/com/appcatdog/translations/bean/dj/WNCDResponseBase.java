package com.appcatdog.translations.bean.dj;



/**
 *
 *
 * Description: 接口返回对象
 */
public class WNCDResponseBase<T> implements NoProguard {

    public int code;
    public T data;
    public String msg;

    @Override
    public String toString() {
        return "Response{" +
                "Code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
