package com.xingyou.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    private Integer code;
    private String message;
    private Object data;
    private Long timestamp;

    /**
     * 成功返回结果（不带数据）
     */
    public static Result success() {
        return new Result(200, "操作成功", null, System.currentTimeMillis());
    }

    /**
     * 成功返回结果（带数据）
     */
    public static Result success(Object data) {
        return new Result(200, "操作成功", data, System.currentTimeMillis());
    }

    /**
     * 成功返回结果（自定义消息和数据）
     */
    public static Result success(String message, Object data) {
        return new Result(200, message, data, System.currentTimeMillis());
    }

    /**
     * 错误返回结果（不带数据）
     */
    public static Result error() {
        return new Result(500, "操作失败", null, System.currentTimeMillis());
    }

    /**
     * 错误返回结果（自定义消息）
     */
    public static Result error(String message) {
        return new Result(500, message, null, System.currentTimeMillis());
    }

    /**
     * 错误返回结果（自定义状态码和消息）
     */
    public static Result error(Integer code, String message) {
        return new Result(code, message, null, System.currentTimeMillis());
    }

    /**
     * 错误返回结果（带数据）
     */
    public static Result error(Integer code, String message, Object data) {
        return new Result(code, message, data, System.currentTimeMillis());
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return this.code != null && this.code == 200;
    }
}
