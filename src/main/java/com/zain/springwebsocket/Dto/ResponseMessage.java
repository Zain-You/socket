package com.zain.springwebsocket.Dto;

import org.springframework.http.HttpStatus;
/*
* controller 统一返回类型
* */
public class ResponseMessage<T> {
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;
    // 返回值 正常：200 ，500 ：内部错误
    private Integer code;

    // 需要返回的数据类型,没有值可以返回null
    private T data;

    @Override
    public String toString() {
        return "ResponseMessage{" +
                "message='" + message + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }

    public ResponseMessage(Integer code,String message,T data)
    {
        this.code=code;
        this.message=message;
        this.data=data;
    }

    public  static <T>  ResponseMessage<T> success(T data)
    {
        return new ResponseMessage(HttpStatus.OK.value(), "success",data);
    }
}
