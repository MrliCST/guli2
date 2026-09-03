package com.atlearn.guli.service;

import com.atlearn.guli.domain.RmeReliableMessage;

import java.util.List;

/**
 * 可靠消息服务接口
 *
 * @author guli
 */
public interface ReliableMessageService {

    /**
     * 保存待发送消息（事务内调用）
     */
    void saveMessage(RmeReliableMessage message);

    /**
     * 发送消息并更新状态
     */
    boolean sendMessage(Long messageId);

    /**
     * 查询超时未发送的消息（用于定时重试）
     */
    List<RmeReliableMessage> findTimeoutMessages();

    /**
     * 删除已确认的消息
     */
    void removeMessage(Long messageId);

    /**
     * 增加重试次数
     */
    void incrementRetry(Long messageId);
}
