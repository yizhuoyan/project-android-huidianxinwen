package com.jason.hdxw.bean;

/**
 * 首页收益实体类
 * created by wang on 2018/11/19
 */
public class HomeEarningsBean {

    /**
     * status : y
     * msg :
     * money : {"jin_day":"0.29991","zong_money":"0.36798","user_money":"476134.65318820"}
     */

    private String status;
    private String msg;
    private MoneyBean money;

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

    public MoneyBean getMoney() {
        return money;
    }

    public void setMoney(MoneyBean money) {
        this.money = money;
    }

    public static class MoneyBean {
        /**
         * jin_day : 0.29991
         * zong_money : 0.36798
         * user_money : 476134.65318820
         */

        private String jin_day;
        private String zong_money;
        private String user_money;
        private String zong;

        public String getJin_day() {
            return jin_day;
        }

        public void setJin_day(String jin_day) {
            this.jin_day = jin_day;
        }

        public String getZong_money() {
            return zong_money;
        }

        public void setZong_money(String zong_money) {
            this.zong_money = zong_money;
        }

        public String getUser_money() {
            return user_money;
        }

        public void setUser_money(String user_money) {
            this.user_money = user_money;
        }

        public String getZong() {
            return zong;
        }

        public void setZong(String zong) {
            this.zong = zong;
        }
    }
}
