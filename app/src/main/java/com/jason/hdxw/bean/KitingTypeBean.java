package com.jason.hdxw.bean;

import java.util.List;

/**
 * 提现类型实体类
 * created by wang on 2018/12/3
 */
public class KitingTypeBean {

    /**
     * list : [{"name":"银行卡","type":"1"},{"name":"支付宝","type":"2"},{"name":"微信","type":"3"}]
     * msg :
     * status : y
     */

    private String msg;
    private String status;
    private List<ListBean> list;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * name : 银行卡
         * type : 1
         */

        private String name;
        private String type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
