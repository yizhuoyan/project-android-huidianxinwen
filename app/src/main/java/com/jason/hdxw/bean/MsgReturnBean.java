package com.jason.hdxw.bean;

import java.util.List;

/**
 * 反馈列表实体类
 * created by wang on 2018/12/4
 */
public class MsgReturnBean {

    /**
     * status : y
     * msg : 反馈列表
     * page : 1
     * count : 1
     * list : [{"id":"74","content":"我要反馈","is_read":"0","user_id":"756","dateline":"1543893009","reply":"回复一下","is_reply":"1","replytime":"1543893052"}]
     */

    private String status;
    private String msg;
    private int page;
    private String count;
    private List<ListBean> list;

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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 74
         * content : 我要反馈
         * is_read : 0
         * user_id : 756
         * dateline : 1543893009
         * reply : 回复一下
         * is_reply : 1
         * replytime : 1543893052
         */

        private String id;
        private String content;
        private String is_read;
        private String user_id;
        private String dateline;
        private String reply;
        private String is_reply;
        private String replytime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getIs_read() {
            return is_read;
        }

        public void setIs_read(String is_read) {
            this.is_read = is_read;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getDateline() {
            return dateline;
        }

        public void setDateline(String dateline) {
            this.dateline = dateline;
        }

        public String getReply() {
            return reply;
        }

        public void setReply(String reply) {
            this.reply = reply;
        }

        public String getIs_reply() {
            return is_reply;
        }

        public void setIs_reply(String is_reply) {
            this.is_reply = is_reply;
        }

        public String getReplytime() {
            return replytime;
        }

        public void setReplytime(String replytime) {
            this.replytime = replytime;
        }
    }
}
