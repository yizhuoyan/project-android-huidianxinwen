package com.jason.hdxw.bean;

/**
 * 微信信息实体类
 * created by wang on 2018/12/3
 */
public class WxMsgBean {

    /**
     * status : y
     * msg :
     * weixin : {"weixin":"","weixin_qr":"http://dianduoduo.jiesenkeji.com/"}
     */

    private String status;
    private String msg;
    private WeixinBean weixin;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public WeixinBean getWeixin() {
        return weixin;
    }

    public void setWeixin(WeixinBean weixin) {
        this.weixin = weixin;
    }

    public static class WeixinBean {
        /**
         * weixin :
         * weixin_qr : http://dianduoduo.jiesenkeji.com/
         */

        private String weixin;
        private String weixin_qr;
        private String weixn_name;
        private String weixin_url;

        public String getWeixin() {
            return weixin;
        }

        public void setWeixin(String weixin) {
            this.weixin = weixin;
        }

        public String getWeixin_qr() {
            return weixin_qr;
        }

        public void setWeixin_qr(String weixin_qr) {
            this.weixin_qr = weixin_qr;
        }

        public String getWeixn_name() {
            return weixn_name;
        }

        public void setWeixn_name(String weixn_name) {
            this.weixn_name = weixn_name;
        }

        public String getWeixin_url() {
            return weixin_url;
        }

        public void setWeixin_url(String weixin_url) {
            this.weixin_url = weixin_url;
        }
    }
}
