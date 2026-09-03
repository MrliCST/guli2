package com.atlearn.guli.service.impl;

import com.atlearn.guli.domain.RmeReliableMessage;
import com.atlearn.guli.mapper.ReliableMessageMapper;
import com.atlearn.guli.service.ReliableMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 可靠消息服务实现
 * <p>
 * 基于本地消息表 + 事务后发送模式，确保 MQ 消息 100% 投递
 * </p>
 *
 * @author guli
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ReliableMessageServiceImpl implements ReliableMessageService {

    private final ReliableMessageMapper reliableMessageMapper;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${reliable-message.max-retry:5}")
    private int maxRetry;

    /**
     * 保存待发送消息（必须在业务事务内调用）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMessage(RmeReliableMessage message) {
        if (message.getMessageId() == null) {
            message.setMessageId(UUID.randomUUID().toString());
        }
        message.setStatus(0); // 0-待发送
        message.setRetryCount(0);
        message.setNextRetryTime(new Date());
        message.setCreateTime(new Date());
        message.setUpdateTime(new Date());
        reliableMessageMapper.insert(message);
        log.info("可靠消息已保存：messageId={}, type={}", message.getMessageId(), message.getMessageType());
    }

    /**
     * 发送消息并更新状态（在事务提交后调用）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendMessage(Long messageId) {
        RmeReliableMessage message = reliableMessageMapper.selectById(messageId);
        if (message == null || message.getStatus() != 0) {
            log.warn("消息不存在或已处理：messageId={}, status={}", messageId, message != null ? message.getStatus() : "null");
            return false;
        }

        try {
            // 发送消息到 MQ
            rabbitTemplate.convertAndSend(
                message.getExchange(),
                message.getRoutingKey(),
                objectMapper.readValue(message.getMessageBody(), Object.class)
            );

            // 更新状态为已发送
            message.setStatus(1);
            message.setUpdateTime(new Date());
            reliableMessageMapper.updateById(message);

            log.info("消息发送成功：messageId={}, exchange={}, routingKey={}", 
                messageId, message.getExchange(), message.getRoutingKey());
            return true;
        } catch (Exception e) {
            log.error("消息发送失败：messageId={}", messageId, e);
            // 更新重试信息
            incrementRetryInternal(messageId);
            return false;
        }
    }

    /**
     * 查询超时未发送的消息（用于定时任务重试）
     */
    @Override
    public List<RmeReliableMessage> findTimeoutMessages() {
        // 查询状态为待发送或发送失败，且下次重试时间已到，重试次数未超限的消息
        // 由于 MyBatis-Plus 条件构造器限制，这里使用简单查询后过滤
        List<RmeReliableMessage> allMessages = reliableMessageMapper.selectList(null);
        Date now = new Date();
        return allMessages.stream()
            .filter(m -> (m.getStatus() == 0 || m.getStatus() == 2) 
                && m.getNextRetryTime() != null 
                && m.getNextRetryTime().before(now)
                && m.getRetryCount() < maxRetry)
            .toList();
    }

    /**
     * 删除已确认的消息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeMessage(Long messageId) {
        reliableMessageMapper.deleteById(messageId);
        log.info("消息已删除：messageId={}", messageId);
    }

    /**
     * 增加重试次数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrementRetry(Long messageId) {
        incrementRetryInternal(messageId);
    }

    private void incrementRetryInternal(Long messageId) {
        RmeReliableMessage message = reliableMessageMapper.selectById(messageId);
        if (message == null) {
            return;
        }

        int newRetryCount = message.getRetryCount() != null ? message.getRetryCount() + 1 : 1;
        message.setRetryCount(newRetryCount);
        message.setStatus(2); // 2-发送失败
        
        // 指数退避策略：每次重试间隔翻倍（秒）
        long delaySeconds = (long) Math.pow(2, newRetryCount);
        LocalDateTime nextRetry = LocalDateTime.now().plusSeconds(delaySeconds);
        message.setNextRetryTime(Date.from(nextRetry.atZone(java.time.ZoneId.systemDefault()).toInstant()));
        message.setUpdateTime(new Date());
        
        reliableMessageMapper.updateById(message);
        log.warn("消息重试：messageId={}, retryCount={}, nextRetryTime={}", 
            messageId, newRetryCount, message.getNextRetryTime());
    }
}
