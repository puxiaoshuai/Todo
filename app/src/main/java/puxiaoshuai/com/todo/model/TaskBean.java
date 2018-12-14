package puxiaoshuai.com.todo.model;

import java.util.List;

/**
 * Created by Administrator on 2018/12/13 0013.
 */

public class TaskBean {

    /**
     * message : 成功
     * code : 200
     * data : [{"id":3,"title":"逸闻趣事","content":"hahahahahah","create_time":"2018-12-13 13:53:39"},{"id":4,"title":"有趣的一件事情2","content":"彩票中奖啦","create_time":"2018-12-13 14:05:46"}]
     */

    private String message;
    private int code;
    private List<DataBean> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 3
         * title : 逸闻趣事
         * content : hahahahahah
         * create_time : 2018-12-13 13:53:39
         */

        private int id;
        private String title;
        private String content;
        private String create_time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
