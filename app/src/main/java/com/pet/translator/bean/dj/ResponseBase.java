package com.pet.translator.bean.dj;



/**
 *
 *
 * Description: 接口返回对象
 */
public class ResponseBase<T> implements NoProguard {

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
