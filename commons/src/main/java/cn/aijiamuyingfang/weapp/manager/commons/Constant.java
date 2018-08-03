package cn.aijiamuyingfang.weapp.manager.commons;

/**
 * Created by pc on 2018/5/6.
 */

public final class Constant {
    /**
     * preference中保存storeId的key
     */
    public static final String PREFERENCE_STORE_ID = "preference_store_id";

    /**
     * intent中保存store的key
     */
    public static final String INTENT_STORE = "intent_store";
    /**
     * intent中保存storeId的key
     */
    public static final String INTENT_STORE_ID = "intent_store_id";
    /**
     * intent中保存topClassifyId的key
     */
    public static final String INTENT_TOP_CLASSIFY_ID = "intent_top_classify_id";
    /**
     * intent中保存subClassifyId的key
     */
    public static final String INTENT_SUB_CLASSIFY_ID = "intent_sub_classify_id";
    /**
     * intent中保存goodId的key
     */
    public static final String INTENT_GOOD = "intent_good";
    /**
     * intent中保存goodId的key
     */
    public static final String INTENT_GOOD_ID = "intent_good_id";

    /**
     * intent中保存message的key
     */
    public static final String INTENT_MESSAGE = "intent_message";

    /**
     * intent中保存goodvoucher的key
     */
    public static final String INTENT_GOODVOUCHER = "intent_goodvoucher";
    /**
     * intent中保存voucheritem的key
     */
    public static final String INTENT_VOUCHERITEM = "intent_voucheritem";

    /**
     * intent中保存fragment name的key
     */
    public static final String INTENT_FRAGMENT_NAME = "intent_fragment_name";

    /**
     * intent中保存fragment启动者的key
     */
    public static final String INTENT_FRAGMENT_FROM = "intent_fragment_from";

    /**
     * intent中保存选中的兑换项的key
     */
    public static final String INTENT_SELECTED_VOUCHERITEM = "intent_selected_voucheritem";


    /**
     * intent中保存选中的兑换券的key
     */
    public static final String INTENT_SELECTED_GOODVOUCHER = "intent_selected_goodvoucher";


    /**
     * EditableImageView打开系统照相机的requestcode
     */
    public static final int REQUEST_IMAGE_BY_CAMERA = 2049;

    /**
     * EditableImageView打开本地相册的requestcode
     */
    public static final int REQUEST_IMAGE_BY_SDCARD = 2050;

    /**
     * AddGoodActivity打开GoodHolderFragemnt的requestcode
     */
    public static final int REQUEST_GOOD_HOLDERCART = 2051;
    /**
     * intent中保存shoporderId的key
     */
    public static final String INTENT_SHOPORDER_ID = "intent_shoporder_id";

    /**
     * intent中保存shoporder的key
     */
    public static final String INTENT_SHOPORDER = "intent_shoporder";
    /**
     * 发送Tab本选择的广播
     */
    public static final String BROADCAST_ACTION_TAB_SELECTED = "tab_selected";
    /**
     * intent中保存MainActivity Tab Index的key
     */
    public static final String INTENT_TAB_INDEX = "tab_index";

    private Constant() {
    }
}
