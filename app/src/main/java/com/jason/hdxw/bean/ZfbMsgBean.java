package com.jason.hdxw.bean;

/**
 * 支付宝信息实体类
 * created by wang on 2018/12/3
 */
public class ZfbMsgBean {

    /**
     * status : y
     * msg :
     * alipay : {"alipay":"","alipay_qr":"http://dianduoduo.jiesenkeji.com/"}
     */

    private String status;
    private String msg;
    private AlipayBean alipay;

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

    public AlipayBean getAlipay() {
        return alipay;
    }

    public void setAlipay(AlipayBean alipay) {
        this.alipay = alipay;
    }

    public static class AlipayBean {
        /**
         * alipay :
         * alipay_qr : http://dianduoduo.jiesenkeji.com/
         */

        private String alipay;
        private String alipay_qr;
        private String alipay_url;
        private String alipay_name;

        public String getAlipay() {
            return alipay;
        }

        public void setAlipay(String alipay) {
            this.alipay = alipay;
        }

        public String getAlipay_qr() {
            return alipay_qr;
        }

        public void setAlipay_qr(String alipay_qr) {
            this.alipay_qr = alipay_qr;
        }

        public String getAlipay_url() {
            return alipay_url;
        }

        public void setAlipay_url(String alipay_url) {
            this.alipay_url = alipay_url;
        }

        public String getAlipay_name() {
            return alipay_name;
        }

        public void setAlipay_name(String alipay_name) {
            this.alipay_name = alipay_name;
        }
    }
}
