package puxiaoshuai.com.todo.event;

public class FreshTaskList {
    private boolean fresh;

    public FreshTaskList(boolean fresh) {
        this.fresh = fresh;
    }

    public boolean isFresh() {
        return fresh;
    }

    public void setFresh(boolean fresh) {
        this.fresh = fresh;
    }
}
