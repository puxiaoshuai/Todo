package puxiaoshuai.com.todo.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/12/13 0013.
 */

public class TaskBean  {

    /**
     * message : 成功
     * code : 200
     * data : {"page":"2","page_size":"2","total_num":10,"list":[{"id":10,"title":"存储此处错错错错错","content":"飞飞飞飞飞凤飞飞","create_time":"2018-12-16 19:27:02"},{"id":9,"title":"测试2","content":"222222222222","create_time":"2018-12-16 19:26:53"}]}
     */

    private String message;
    private int code;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * page : 2
         * page_size : 2
         * total_num : 10
         * list : [{"id":10,"title":"存储此处错错错错错","content":"飞飞飞飞飞凤飞飞","create_time":"2018-12-16 19:27:02"},{"id":9,"title":"测试2","content":"222222222222","create_time":"2018-12-16 19:26:53"}]
         */

        private String page;
        private String page_size;
        private int total_num;
        private List<ListBean> list;

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getPage_size() {
            return page_size;
        }

        public void setPage_size(String page_size) {
            this.page_size = page_size;
        }

        public int getTotal_num() {
            return total_num;
        }

        public void setTotal_num(int total_num) {
            this.total_num = total_num;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean implements Serializable {
            /**
             * id : 10
             * title : 存储此处错错错错错
             * content : 飞飞飞飞飞凤飞飞
             * create_time : 2018-12-16 19:27:02
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
}
