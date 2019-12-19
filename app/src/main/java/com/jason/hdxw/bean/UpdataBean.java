package com.jason.hdxw.bean;

/**
 * 升级版本实体类
 * created by wang on 2018/11/21
 */
public class UpdataBean {

    /**
     * status : y
     * msg :
     * list : {"message":"请下载安装新版本！","link_url":"http://192.168.1.119:7008/","varsionnum":"1.0","down_url":"http://192.168.1.119:7008/version/tiantianzhuan_v_1.0.apk","tag":0,"is_update":0}
     */

    private String status;
    private String msg;
    private ListBean list;

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

    public ListBean getList() {
        return list;
    }

    public void setList(ListBean list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * message : 请下载安装新版本！
         * link_url : http://192.168.1.119:7008/
         * varsionnum : 1.0
         * down_url : http://192.168.1.119:7008/version/tiantianzhuan_v_1.0.apk
         * tag : 0
         * is_update : 0
         */

        private String message;
        private String link_url;
        private String varsionnum;
        private String down_url;
        private int tag;
        private int is_update;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getLink_url() {
            return link_url;
        }

        public void setLink_url(String link_url) {
            this.link_url = link_url;
        }

        public String getVarsionnum() {
            return varsionnum;
        }

        public void setVarsionnum(String varsionnum) {
            this.varsionnum = varsionnum;
        }

        public String getDown_url() {
            return down_url;
        }

        public void setDown_url(String down_url) {
            this.down_url = down_url;
        }

        public int getTag() {
            return tag;
        }

        public void setTag(int tag) {
            this.tag = tag;
        }

        public int getIs_update() {
            return is_update;
        }

        public void setIs_update(int is_update) {
            this.is_update = is_update;
        }
    }
}
