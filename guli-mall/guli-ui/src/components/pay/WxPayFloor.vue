<template>
  <section class="wxPayFloor">
    <div class="wxPayFloor__inner">
      <!-- 提示栏 -->
      <div class="noticeBox">
        <div class="noticeBox__left">
          <span class="successIcon">✓</span>
          <span class="successInfo">订单提交成功，请您及时付款！订单号：56789065645</span>
        </div>
        <div class="noticeBox__right">
          <em>应付金额：<b class="money">￥17,654</b>元</em>
        </div>
      </div>

      <!-- 微信扫码支付区 -->
      <div class="wxPayArea">
        <div class="wxPayArea__label">微信支付</div>
        <div class="wxPayArea__body">
          <div class="qrSection">
            <div class="qrBox">
              <div class="qrPlaceholder">
                <img :src="'/img/wx_cz.jpg'" alt="微信支付二维码" />
              </div>
              <div class="scanTip">
                <p>请使用微信扫一扫</p>
                <p>扫描二维码支付</p>
              </div>
            </div>
            <div class="phonePreview">
              <div class="phoneMockup">
                <div class="phoneScreen">
                  <p class="wxTitle">微信支付</p>
                  <p class="wxAmount">￥17,654</p>
                  <div class="wxQr"></div>
                  <p class="wxTip">请确认微信支付</p>
                </div>
              </div>
            </div>
          </div>
        </div>
        <p class="otherLink">
          <router-link to="/pay">> 其他支付方式</router-link>
        </p>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
</script>

<style scoped>
.wxPayFloor { background: #f5f5f5; padding: 10px 0; }
.wxPayFloor__inner {
  width: 1200px;
  margin: 0 auto;
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  display: grid;
  gap: 16px;
}

/* 提示栏 */
.noticeBox {
  display: grid;
  grid-template-columns: 1fr auto;
  gap: 20px;
  align-items: center;
  background: #fffbe6;
  border: 1px solid #ffe58f;
  border-radius: 6px;
  padding: 16px;
}
.noticeBox__left { display: flex; align-items: center; gap: 12px; }
.successIcon {
  width: 32px; height: 32px;
  background: #4CAF50; color: #fff;
  border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  font-size: 16px; font-weight: bold;
}
.successInfo { font-size: 16px; font-weight: bold; color: #333; }
.noticeBox__right { text-align: right; font-size: 14px; color: #666; }
.noticeBox__right em { font-style: normal; }
.money { color: #c81623; font-size: 22px; font-weight: bold; }

/* 微信支付区 */
.wxPayArea { padding: 20px 0; }
.wxPayArea__label {
  font-size: 18px;
  font-weight: bold;
  color: #07c160;
  padding: 8px 16px;
  border-left: 4px solid #07c160;
}
.wxPayArea__body { padding: 30px 0; }

.qrSection {
  display: grid;
  grid-template-columns: auto auto;
  gap: 60px;
  align-items: center;
  justify-content: center;
}

/* 二维码区 */
.qrBox {
  display: flex;
  align-items: center;
  gap: 20px;
}
.qrPlaceholder {
  width: 200px;
  height: 200px;
  border: 1px solid #eee;
  border-radius: 8px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px;
}
.qrPlaceholder img { width: 100%; height: 100%; object-fit: contain; }
.scanTip p { margin: 0; font-size: 14px; color: #666; line-height: 1.6; }

/* 手机预览 */
.phonePreview { display: flex; justify-content: center; }
.phoneMockup {
  width: 200px;
  height: 360px;
  border: 4px solid #333;
  border-radius: 20px;
  overflow: hidden;
  background: #f5f5f5;
}
.phoneScreen {
  height: 100%;
  display: grid;
  grid-template-rows: auto auto 1fr auto;
  gap: 8px;
  padding: 16px;
  text-align: center;
  align-items: center;
}
.wxTitle { font-size: 14px; color: #333; font-weight: bold; margin: 0; }
.wxAmount { font-size: 22px; color: #333; font-weight: bold; margin: 0; }
.wxQr {
  width: 120px;
  height: 120px;
  background: repeating-conic-gradient(#333 0% 25%, #fff 0% 50%);
  background-size: 12px 12px;
  border-radius: 4px;
  margin: 0 auto;
}
.wxTip { font-size: 12px; color: #999; margin: 0; }

.otherLink { text-align: center; padding-top: 20px; }
.otherLink a { font-size: 13px; color: #666; text-decoration: none; }
.otherLink a:hover { color: #c81623; }
</style>
