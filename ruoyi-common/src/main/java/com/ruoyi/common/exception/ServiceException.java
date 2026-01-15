package com.ruoyi.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * 业务异常
 *
 * @author ruoyi
 */
@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
public final class ServiceException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误信息对象
     */
    private Object data;

    /**
     * 错误明细，内部调试错误
     *
     */
    private String detailMessage;

    /**
     * 空构造方法，避免反序列化问题
     */

    public ServiceException(String message)
    {
        this.message = message;
    }

    public ServiceException(String message, Integer code)
    {
        this.message = message;
        this.code = code;
    }

    public ServiceException(String message, Integer code, Object data)
    {
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public ServiceException(Integer code, Object data)
    {
        this.code = code;
        this.data = data;
    }

    public ServiceException(String message, Object data)
    {
        this.message = message;
        this.data = data;
    }
    
}
