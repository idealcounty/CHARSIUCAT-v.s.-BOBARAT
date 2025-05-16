<script setup>
</script>

<template>
  <div class="footer">
    <div class="footer_content">
      <span id="footerLogo">
        <img src="https://community.cloudflare.steamstatic.com/public/images/skin_1/footerLogo_valve.png?v=1" width="96" height="26" border="0" alt="Valve 徽标">
      </span>
      <span id="footerText">
            © Valve Corporation。保留所有权利。所有商标均为其在美国及其它国家/地区的各自持有者所有。 本网站上部分地理空间数据由
        <a href="https://steamcommunity.com/linkfilter/?u=http%3A%2F%2Fwww.geonames.org" target="_blank" rel=" noopener">geonames.org
        </a> 提供。					<br>
        <span class="valve_links">
          <a href="http://store.steampowered.com/privacy_agreement/" target="_blank">隐私政策</a>
          &nbsp;| &nbsp;<a href="https://store.steampowered.com/legal/" target="_blank">法律信息</a>
          &nbsp;| &nbsp;<a href="http://store.steampowered.com/subscriber_agreement/" target="_blank">Steam 订户协议</a>
          &nbsp;| &nbsp;<a href="http://store.steampowered.com/account/cookiepreferences/" target="_blank">Cookie</a>
        </span>
      </span>
    </div>
  </div>
</template>

<style scoped>
.footer {
  position: relative;
  background-color: #000000;
  left: 0;
  right: 0;
  bottom: 0;
  margin: 0;
  padding: 16px 0;
  text-align: left;
  width: auto;
  font-size: 12px;
  color: #8F98A0;
  border: 0;
}

.footer_content {
  position: relative;
  width: 958px;
  margin: 0 auto;
}

#footerLogo {
  position: absolute;
  left: 16px;
  top: 4px;
}

#footerText {
  padding: 2px 0 0 123px;
  line-height: 14px;
  display: block;
  color: #8F98A0;
}

a {
  color: #C6D4DF;
  text-decoration: none;
  position: relative;
  text-align: left;
  width: auto;
  font-size: 12px;
  border: 0;
  margin: 0;
  padding: 0;

  &:hover {
    color: #ffffff;
  }
}

.valve_links {
  margin: 15px 0 0 0;
  display: block;
}
</style>
