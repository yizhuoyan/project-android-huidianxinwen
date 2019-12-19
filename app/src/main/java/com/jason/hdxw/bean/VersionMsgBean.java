package com.jason.hdxw.bean;

/**
 * 版本信息实体类
 * created by wang on 2018/11/19
 */
public class VersionMsgBean {

    /**
     * status : y
     * msg :
     * about : {"email":"123456@qq.com","tel":"123456","banben":"1.0"}
     */

    private String status;
    private String msg;
    private AboutBean about;

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

    public AboutBean getAbout() {
        return about;
    }

    public void setAbout(AboutBean about) {
        this.about = about;
    }

    public static class AboutBean {
        /**
         * email : 123456@qq.com
         * tel : 123456
         * banben : 1.0
         */

        private String email;
        private String tel;
        private String banben;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getBanben() {
            return banben;
        }

        public void setBanben(String banben) {
            this.banben = banben;
        }
    }
}
