package cn.aijiamuyingfang.weapp.manager.widgets;

/**
 * 选项卡信息
 * Created by pc on 2018/3/30.
 */
public class Tab {

    /**
     * 选项卡标签
     */
    private int title;
    /**
     * 选项卡图标
     */
    private int icon;
    /**
     * 选项卡关联的Fragment
     */
    private Class fragment;

    public Tab(int title, int icon, Class fragment) {
        this.title = title;
        this.icon = icon;
        this.fragment = fragment;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Class getFragment() {
        return fragment;
    }

    public void setFragment(Class fragment) {
        this.fragment = fragment;
    }
}
