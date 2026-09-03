package com.atlearn.guli.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 可靠消息对象
 *
 * @author guli
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("rme_reliable_message")
public class RmeReliableMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息 ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 消息唯一标识（业务幂等键）
     */
    private String messageId;

    /**
     * 交换机
     */
    private String exchange;

    /**
     * 路由键
     */
    private String routingKey;

    /**
     * 消息内容（JSON）
     */
    private String messageBody;

    /**
     * 消息类型（ORDER_CREATE, ORDER_CANCEL 等）
     */
    private String messageType;

    /**
     * 状态：0-待发送，1-已发送，2-发送失败
     */
    private Integer status;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 下次重试时间
     */
    private Date nextRetryTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
