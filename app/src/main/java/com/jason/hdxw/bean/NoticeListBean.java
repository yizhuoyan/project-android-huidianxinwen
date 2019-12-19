package com.jason.hdxw.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 公告列表实体类
 * created by wang on 2018/11/19
 */
public class NoticeListBean implements Serializable {

    /**
     * status : y
     * msg :
     * select : [{"id":"1","title":"关于实名认证通知","starttime":"2018-10-25 10:25:50","content":"暂时后台实名认证要登陆RSC网址htt://rsc58.co"},{"id":"2","title":"生态农业链优势","starttime":"2018-10-25 10:25:31","content":"矿机是整个项目中开采RSC数字资产的工具，整个ESTT衡量发"},{"id":"3","title":"RSC正式上线启动","starttime":"2018-10-25 10:25:01","content":"2018年11月28日，平台预售矿机活动正式结束，RSC面向"}]
     */

    private String status;
    private String msg;
    private List<SelectBean> select;

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

    public List<SelectBean> getSelect() {
        return select;
    }

    public void setSelect(List<SelectBean> select) {
        this.select = select;
    }

    public static class SelectBean implements Serializable{
        /**
         * id : 1
         * title : 关于实名认证通知
         * starttime : 2018-10-25 10:25:50
         * content : 暂时后台实名认证要登陆RSC网址htt://rsc58.co
         */

        private String id;
        private String title;
        private String starttime;
        private String content;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
