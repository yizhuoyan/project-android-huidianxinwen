package com.jason.hdxw.bean;

import java.io.Serializable;

/**
 * 用户信息实体类
 * created by wang on 2018/11/19
 */
public class UserInfoBean implements Serializable {

    /**
     * status : y
     * msg :
     * user_find : {"id":"745","username":"test","mobile_phone":"17615855490","miner_type":"0","invite_id":"388","invite_str":",388,","reg_time":"0","user_money":"0.2660","true_name":"王春雷","idcard":"","ico":"/template/wap/statics/yangs/images/header.jpg","freeze_money":"0.00000000","bankaddress":"","banknum":"","is_renzheng":"0","invite_code":"N404YE","wx_code":"http://192.168.1.119:7008/uploadfile/code_img/N404YE/N404YE.png","invite_name":"18253172708"}
     */

    private String status;
    private String msg;
    private UserFindBean user_find;

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

    public UserFindBean getUser_find() {
        return user_find;
    }

    public void setUser_find(UserFindBean user_find) {
        this.user_find = user_find;
    }

    public static class UserFindBean implements Serializable {
        /**
         * id : 745
         * username : test
         * mobile_phone : 17615855490
         * miner_type : 0
         * invite_id : 388
         * invite_str : ,388,
         * reg_time : 0
         * user_money : 0.2660
         * true_name : 王春雷
         * idcard :
         * ico : /template/wap/statics/yangs/images/header.jpg
         * freeze_money : 0.00000000
         * bankaddress :
         * banknum :
         * is_renzheng : 0
         * invite_code : N404YE
         * wx_code : http://192.168.1.119:7008/uploadfile/code_img/N404YE/N404YE.png
         * invite_name : 18253172708
         */

        private String id;
        private String username;
        private String mobile_phone;
        private String miner_type;
        private String invite_id;
        private String invite_str;
        private String reg_time;
        private String user_money;
        private String true_name;
        private String idcard;
        private String ico;
        private String freeze_money;
        private String bankaddress;
        private String banknum;
        private String is_renzheng;
        private String invite_code;
        private String wx_code;
        private String invite_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getMobile_phone() {
            return mobile_phone;
        }

        public void setMobile_phone(String mobile_phone) {
            this.mobile_phone = mobile_phone;
        }

        public String getMiner_type() {
            return miner_type;
        }

        public void setMiner_type(String miner_type) {
            this.miner_type = miner_type;
        }

        public String getInvite_id() {
            return invite_id;
        }

        public void setInvite_id(String invite_id) {
            this.invite_id = invite_id;
        }

        public String getInvite_str() {
            return invite_str;
        }

        public void setInvite_str(String invite_str) {
            this.invite_str = invite_str;
        }

        public String getReg_time() {
            return reg_time;
        }

        public void setReg_time(String reg_time) {
            this.reg_time = reg_time;
        }

        public String getUser_money() {
            return user_money;
        }

        public void setUser_money(String user_money) {
            this.user_money = user_money;
        }

        public String getTrue_name() {
            return true_name;
        }

        public void setTrue_name(String true_name) {
            this.true_name = true_name;
        }

        public String getIdcard() {
            return idcard;
        }

        public void setIdcard(String idcard) {
            this.idcard = idcard;
        }

        public String getIco() {
            return ico;
        }

        public void setIco(String ico) {
            this.ico = ico;
        }

        public String getFreeze_money() {
            return freeze_money;
        }

        public void setFreeze_money(String freeze_money) {
            this.freeze_money = freeze_money;
        }

        public String getBankaddress() {
            return bankaddress;
        }

        public void setBankaddress(String bankaddress) {
            this.bankaddress = bankaddress;
        }

        public String getBanknum() {
            return banknum;
        }

        public void setBanknum(String banknum) {
            this.banknum = banknum;
        }

        public String getIs_renzheng() {
            return is_renzheng;
        }

        public void setIs_renzheng(String is_renzheng) {
            this.is_renzheng = is_renzheng;
        }

        public String getInvite_code() {
            return invite_code;
        }

        public void setInvite_code(String invite_code) {
            this.invite_code = invite_code;
        }

        public String getWx_code() {
            return wx_code;
        }

        public void setWx_code(String wx_code) {
            this.wx_code = wx_code;
        }

        public String getInvite_name() {
            return invite_name;
        }

        public void setInvite_name(String invite_name) {
            this.invite_name = invite_name;
        }
    }
}
