package com.atlearn.guli.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 登录响应
 *
 * @author guli
 */
@Data
public class LoginVo {

    /**
     * 授权令牌
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * 令牌有效期（秒）
     */
    @JsonProperty("expire_in")
    private Long expireIn;

    /**
     * 会员id
     */
    @JsonProperty("member_id")
    private Long memberId;

}
