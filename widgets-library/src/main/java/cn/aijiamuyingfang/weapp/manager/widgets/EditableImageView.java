package cn.aijiamuyingfang.weapp.manager.widgets;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.weapp.manager.commons.CommonApp;
import cn.aijiamuyingfang.weapp.manager.commons.Constant;
import cn.aijiamuyingfang.weapp.manager.commons.R;
import cn.aijiamuyingfang.weapp.manager.commons.activity.PermissionActivity;
import cn.aijiamuyingfang.weapp.manager.commons.utils.FileUtils;
import cn.aijiamuyingfang.weapp.manager.commons.utils.IOUtils;
import cn.aijiamuyingfang.weapp.manager.commons.utils.PermissionUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by pc on 2018/4/2.、
 * 一个圆形的头像控件，点击后会弹出选择照片或者拍照的对话框
 */

public class EditableImageView extends android.support.v7.widget.AppCompatImageView {
    private static final String TAG = EditableImageView.class.getName();

    private String IMG_TARGET_DIR = CommonApp.getApplication().getDefaultImageDir();

    private String IMG_DEFAULT = IMG_TARGET_DIR + File.separator + "default.jpg";

    private static final String PIC_AFTER_CROP = "pic_after_crop_";


    //对话框的对象
    private AvatarDialog avatarDialog;
    private View avatarRootView;
    //Activity的上下文
    private Context mContext;

    private Uri mShareUri;
    private File mImageFile;
    private String mImageURL;

    /**
     * 写存储的权限是否被赋予
     */
    private boolean writeExternalGranted = false;

    /**
     * 是否有startActivityForResult活动没有被onActivityResult方法处理
     */
    private boolean mInEdit = false;

    public EditableImageView(Context context) {
        this(context, null);
    }


    public EditableImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        //在编辑模式下不需要进行初始化
        if (this.isInEditMode()) {
            return;
        }
        //初始化点击事件
        initClickListener();

        PermissionActivity.checkAndRequestPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionUtils.PermissionGrantedCallBack() {
            @Override
            public void onPermissionGranted() {
                writeExternalGranted = true;
                //初始化文件相关
                init();
            }
        });
    }

    public void init() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) {
                //初始化图片目录
                initIMGDir();
                //初始化默认图片
                initDefaultIMG();
                //初始化图片文件
                initFile();
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) {
                Log.i(TAG, "init finished");
            }
        });
    }


    private void initIMGDir() {
        File imgDir = new File(IMG_TARGET_DIR);
        if (imgDir.exists()) {
            return;
        }
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            FileUtils.createDirectory(imgDir);
        }
    }

    private void initDefaultIMG() {
        File defaultImg = new File(IMG_DEFAULT);
        if (defaultImg.exists()) {
            return;
        }

        Drawable drawable = CommonApp.getApplication().getDrawable(R.drawable.company_logo);
        if (drawable != null) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            try {
                IOUtils.write(IMG_DEFAULT, stream.toByteArray());
            } catch (IOException e) {
                Log.e(TAG, "create default img:" + IMG_DEFAULT + ",failed", e);
            }
        }
    }

    //设置目录 存放图片
    public void initFile() {
        // 输出裁剪的临时文件的时间
        long time = new Date().getTime();

        /*
          因 Android 7.0 开始，不能使用 file:// 类型的 Uri 访问跨应用文件，否则报异常，
          因此我们这里需要使用内容提供器，FileProvider 是 ContentProvider 的一个子类，
          我们可以轻松的使用 FileProvider 来在不同程序之间分享数据(相对于 ContentProvider 来说)
         */
        // 照片命名
        String origFileName = "pic_origin_" + time + ".jpg";
        if (Build.VERSION.SDK_INT >= 24) {
            mShareUri = FileProvider.getUriForFile(mContext, "cn.aijiamuyingfang.weapp.manager", new File(IMG_TARGET_DIR, origFileName));
        } else {
            mShareUri = Uri.fromFile(new File(IMG_TARGET_DIR, origFileName)); // Android 7.0 以前使用原来的方法来获取文件的 Uri
        }
    }


    /**
     * 初始化点击事件
     * 点击的效果为弹出对话框选择拍照或者从相册获取
     */
    private void initClickListener() {
        avatarDialog = new AvatarDialog(EditableImageView.this.mContext);
        avatarDialog.setPositiveListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                startActionCamera(mShareUri);
                avatarDialog.dismiss();
            }
        });
        avatarDialog.setNegativeListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                startActionPickCrop();
                avatarDialog.dismiss();
            }
        });

        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (writeExternalGranted) {
                    avatarDialog.setCancelable(true);
                    avatarDialog.show();
                } else {
                    PermissionActivity.checkAndRequestPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionUtils.PermissionGrantedCallBack() {
                        @Override
                        public void onPermissionGranted() {
                            writeExternalGranted = true;
                            //初始化文件相关
                            init();
                            avatarDialog.setCancelable(true);
                            avatarDialog.show();
                        }
                    });
                }
            }
        });
    }

    /**
     * 调用相机拍照
     */
    private void startActionCamera(Uri output) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用系统照相机
        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        this.mInEdit = true;
        ((Activity) this.mContext).startActivityForResult(intent,
                Constant.REQUEST_IMAGE_BY_CAMERA);
    }

    /**
     * 选择图片 图片相册
     */
    private void startActionPickCrop() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        this.mInEdit = true;
        ((Activity) this.mContext).startActivityForResult(intent,
                Constant.REQUEST_IMAGE_BY_SDCARD);
    }

    /**
     * 拍照后裁剪
     *
     * @param input  原始图片
     * @param output 裁剪后图片
     */
    public void startActionCrop(Uri input, Uri output) {
        this.mInEdit = true;
        UCrop.of(input, output)
                .withAspectRatio(1, 1)
                .withMaxResultSize(500, 500)
                .start((Activity) mContext);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!isInEdit()) {
            return;
        }
        mInEdit = false;
        switch (requestCode) {
            case Constant.REQUEST_IMAGE_BY_CAMERA:
                if (Activity.RESULT_OK == resultCode) {
                    long time = new Date().getTime();
                    String cropFileName = PIC_AFTER_CROP + time + ".jpg";
                    mImageFile = new File(IMG_TARGET_DIR + cropFileName);
                    // 拍照后裁剪
                    startActionCrop(mShareUri, Uri.fromFile(mImageFile));
                }
                break;
            case Constant.REQUEST_IMAGE_BY_SDCARD:
                // 请求相册后，裁剪
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        long time = new Date().getTime();
                        String cropFileName = PIC_AFTER_CROP + time + ".jpg";
                        mImageFile = new File(IMG_TARGET_DIR + cropFileName);
                        startActionCrop(uri, Uri.fromFile(mImageFile));
                    }
                }
                break;
            case UCrop.REQUEST_CROP:
                //更新头像
                if (resultCode == Activity.RESULT_OK) {
                    final Uri resultUri = UCrop.getOutput(data);
                    this.setImageURI(resultUri);
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Log.i(TAG, "cancel crop image");
                } else {
                    final Throwable cropError = UCrop.getError(data);
                    Log.e(TAG, "crop image failed", cropError);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        //使点击事件设置无效
    }


    public boolean isInEdit() {
        return this.mInEdit;
    }

    public void setImageUrl(String imageURL) {
        this.mImageURL = imageURL;
    }

    /**
     * @return 返回处理后的图片
     */
    public File getImageFileSync() {
        if (null == mImageFile) {
            long time = new Date().getTime();
            String cropFileName = PIC_AFTER_CROP + time + ".jpg";
            // 裁剪头像的绝对路径
            mImageFile = new File(IMG_TARGET_DIR + cropFileName);
        }
        if (!mImageFile.exists() && !StringUtils.isEmpty(mImageURL)) {
            try {
                Bitmap bitmap = GlideUtils.getBitmap(mContext, mImageURL);
                FileUtils.saveBitmap(bitmap, mImageFile);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        if (mImageFile.exists()) {
            return mImageFile;
        }
        return new File(IMG_DEFAULT);
    }

    /**
     * 显示选项的对话框
     * 提示用户选择拍照或者从相册选择
     */
    public class AvatarDialog extends AlertDialog {

        //按钮的点击事件
        private OnClickListener mPhotoButtonListener;
        private OnClickListener mChoosePicListener;


        //保存上下文
        Context mContext;

        private AvatarDialog(Context context) {
            super(context);
            this.mContext = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            super.onCreate(savedInstanceState);
            Window window = getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(0));
                WindowManager.LayoutParams params = window.getAttributes();
                int width = getContext().getResources().getDisplayMetrics().widthPixels;
                params.width = (int) (width * 4f / 5);
                window.setAttributes(params);
            }
            if (avatarRootView == null) {
                avatarRootView = LayoutInflater.from(mContext).inflate(R.layout.editable_image_view, null);
            }
            //拍照按钮控件
            TextView mPhotoBtn = avatarRootView.findViewById(R.id.tv_photo_btn);
            mPhotoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PermissionActivity.checkAndRequestPermission(mContext, Manifest.permission.CAMERA, new PermissionUtils.PermissionGrantedCallBack() {
                        @Override
                        public void onPermissionGranted() {
                            dismiss();
                            if (mPhotoButtonListener != null) {
                                mPhotoButtonListener.onClick(AvatarDialog.this, BUTTON_POSITIVE);
                            }
                        }
                    });
                }
            });
            //选择图片控件
            TextView mChoosePicBtn = avatarRootView.findViewById(R.id.tv_choosepic_btn);
            mChoosePicBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (mChoosePicListener != null) {
                        mChoosePicListener.onClick(AvatarDialog.this, BUTTON_NEGATIVE);
                    }
                }
            });
            setContentView(avatarRootView);
        }


        /**
         * 拍照
         */
        void setPositiveListener(final OnClickListener listener) {
            mPhotoButtonListener = listener;
        }

        /**
         * 选择照片
         */
        void setNegativeListener(final OnClickListener listener) {
            mChoosePicListener = listener;
        }
    }
}
