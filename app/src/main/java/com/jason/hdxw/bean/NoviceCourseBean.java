package com.jason.hdxw.bean;

import java.util.List;

/**
 * 新手教程实体类
 * created by wang on 2018/11/19
 */
public class NoviceCourseBean {

    /**
     * status : y
     * msg :
     * select : [{"id":"7","title":"新手解答2","create_time":"2018-11-08 17:13:36","content":"新手解答2新手解答2新手解答2新手解答2新手解答2新手解答2"},{"id":"6","title":"新手解答","create_time":"2018-11-08 17:12:32","content":"新手解答新手解答新手解答新手解答新手解答新手解答新手解答新手"}]
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

    public static class SelectBean {
        /**
         * id : 7
         * title : 新手解答2
         * create_time : 2018-11-08 17:13:36
         * content : 新手解答2新手解答2新手解答2新手解答2新手解答2新手解答2
         */

        private String id;
        private String title;
        private String create_time;
        private String content;
        private boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

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

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
