package com.jason.hdxw.bean;

import java.util.List;

/**
 * 我的团队实体类
 * created by wang on 2018/11/20
 */
public class MyTeamBean {

    /**
     * status : y
     * msg :
     * page : 1
     * count : {"zong":"65","yi_count":"9","er_count":"21","san_count":"23"}
     * user_withdraw : [{"ico":"http://192.168.1.119:7008//uploadfile/avatar/652_200.jpg","username":"金庸","reg_time":"2018-10-31 17:44:18"},{"ico":"http://192.168.1.119:7008/","username":"18253652652","reg_time":"2018-09-28 18:44:56"},{"ico":"http://192.168.1.119:7008/","username":"时空猎人","reg_time":"2018-09-17 15:29:26"},{"ico":"http://192.168.1.119:7008/","username":"龙井","reg_time":"2018-09-17 14:53:08"},{"ico":"http://192.168.1.119:7008/","username":"小龙女","reg_time":"2018-09-17 14:41:15"},{"ico":"http://192.168.1.119:7008/","username":"黄蓉","reg_time":"2018-09-17 14:32:53"},{"ico":"http://192.168.1.119:7008/","username":"郭靖","reg_time":"2018-09-17 14:11:17"},{"ico":"http://192.168.1.119:7008//template/wap/statics/yangs/images/header.jpg","username":"空军建军节cfm","reg_time":"1970-01-01 08:00:00"},{"ico":"http://192.168.1.119:7008/./uploadfile/avatar/5bf3691bb4c31.png","username":"test","reg_time":"1970-01-01 08:00:00"}]
     */

    private String status;
    private String msg;
    private int page;
    private CountBean count;
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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public CountBean getCount() {
        return count;
    }

    public void setCount(CountBean count) {
        this.count = count;
    }

    public List<UserWithdrawBean> getUser_withdraw() {
        return user_withdraw;
    }

    public void setUser_withdraw(List<UserWithdrawBean> user_withdraw) {
        this.user_withdraw = user_withdraw;
    }

    public static class CountBean {
        /**
         * zong : 65
         * yi_count : 9
         * er_count : 21
         * san_count : 23
         */

        private String zong;
        private String yi_count;
        private String er_count;
        private String san_count;

        public String getZong() {
            return zong;
        }

        public void setZong(String zong) {
            this.zong = zong;
        }

        public String getYi_count() {
            return yi_count;
        }

        public void setYi_count(String yi_count) {
            this.yi_count = yi_count;
        }

        public String getEr_count() {
            return er_count;
        }

        public void setEr_count(String er_count) {
            this.er_count = er_count;
        }

        public String getSan_count() {
            return san_count;
        }

        public void setSan_count(String san_count) {
            this.san_count = san_count;
        }
    }

    public static class UserWithdrawBean {
        /**
         * ico : http://192.168.1.119:7008//uploadfile/avatar/652_200.jpg
         * username : 金庸
         * reg_time : 2018-10-31 17:44:18
         */

        private String ico;
        private String username;
        private String reg_time;

        public String getIco() {
            return ico;
        }

        public void setIco(String ico) {
            this.ico = ico;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getReg_time() {
            return reg_time;
        }

        public void setReg_time(String reg_time) {
            this.reg_time = reg_time;
        }
    }
}
