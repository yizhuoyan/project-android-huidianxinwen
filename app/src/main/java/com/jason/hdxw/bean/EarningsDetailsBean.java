package com.jason.hdxw.bean;

import java.util.List;

/**
 * 收益明细实体类
 * created by wang on 2018/11/20
 */
public class EarningsDetailsBean {

    /**
     * status : y
     * msg :
     * user_withdraw : [{"id":"25","user_id":"388","money":"0.97651","time":"1542007988","status":"1","fa_time":"1542038464","dateline":"2018-11-12 15:33:08","msg":"浏览收益"}]
     * num : 3
     * page : 3
     * zong_money : 60190.2794
     * jin_money : 0
     */

    private String status;
    private String msg;
    private String num;
    private int page;
    private double zong_money;
    private double jin_money;
    private List<UserWithdrawBean> user_withdraw;

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

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public double getZong_money() {
        return zong_money;
    }

    public void setZong_money(double zong_money) {
        this.zong_money = zong_money;
    }

    public double getJin_money() {
        return jin_money;
    }

    public void setJin_money(double jin_money) {
        this.jin_money = jin_money;
    }

    public List<UserWithdrawBean> getUser_withdraw() {
        return user_withdraw;
    }

    public void setUser_withdraw(List<UserWithdrawBean> user_withdraw) {
        this.user_withdraw = user_withdraw;
    }

    public static class UserWithdrawBean {
        /**
         * id : 25
         * user_id : 388
         * money : 0.97651
         * time : 1542007988
         * status : 1
         * fa_time : 1542038464
         * dateline : 2018-11-12 15:33:08
         * msg : 浏览收益
         */

        private String id;
        private String user_id;
        private String money;
        private String time;
        private String status;
        private String fa_time;
        private String dateline;
        private String msg;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getFa_time() {
            return fa_time;
        }

        public void setFa_time(String fa_time) {
            this.fa_time = fa_time;
        }

        public String getDateline() {
            return dateline;
        }

        public void setDateline(String dateline) {
            this.dateline = dateline;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
