package com.jason.hdxw.bean;

import java.util.List;

/**
 * 提现明细实体类
 * created by wang on 2018/11/20
 */
public class KitingDetailsBean {

    /**
     * status : y
     * msg :
     * user_withdraw : [{"id":"12886","user_id":"652","money":"100.00000000","msg":"提现,手续费：10","dateline":"2018-11-08 16:20:05","jiajian":"2"},{"id":"12899","user_id":"652","money":"1000.00000000","msg":"提现,手续费：100","dateline":"2018-11-09 20:44:50","jiajian":"2"},{"id":"12932","user_id":"652","money":"100.00000000","msg":"提现,手续费：10","dateline":"2018-11-11 10:34:09","jiajian":"2"},{"id":"12933","user_id":"652","money":"100.00000000","msg":"提现,手续费：10","dateline":"2018-11-11 10:34:09","jiajian":"2"},{"id":"12934","user_id":"652","money":"100.00000000","msg":"提现,手续费：10","dateline":"2018-11-11 10:34:43","jiajian":"2"},{"id":"12963","user_id":"652","money":"100000.00000000","msg":"提现,手续费：10000","dateline":"2018-11-13 10:56:22","jiajian":"2"}]
     */

    private String status;
    private String msg;
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

    public List<UserWithdrawBean> getUser_withdraw() {
        return user_withdraw;
    }

    public void setUser_withdraw(List<UserWithdrawBean> user_withdraw) {
        this.user_withdraw = user_withdraw;
    }

    public static class UserWithdrawBean {
        /**
         * id : 12886
         * uid : 652
         * money : 100.00000000
         * msg : 提现,手续费：10
         * dateline : 2018-11-08 16:20:05
         * withdraw_status :1
         */

        private String id;
        private String uid;
        private String money;
        private String msg;
        private String withdraw_status;
        private String dateline;
        private String type_msg;

        public String getType_msg() {
            return type_msg;
        }

        public void setType_msg(String type_msg) {
            this.type_msg = type_msg;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getWithdraw_status() {
            return withdraw_status;
        }

        public void setWithdraw_status(String withdraw_status) {
            this.withdraw_status = withdraw_status;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }


        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getDateline() {
            return dateline;
        }

        public void setDateline(String dateline) {
            this.dateline = dateline;
        }

    }
}
