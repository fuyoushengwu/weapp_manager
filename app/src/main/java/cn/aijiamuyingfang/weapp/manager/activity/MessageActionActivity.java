package cn.aijiamuyingfang.weapp.manager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import cn.aijiamuyingfang.client.rest.api.UserMessageControllerApi;
import cn.aijiamuyingfang.commons.domain.response.ResponseBean;
import cn.aijiamuyingfang.commons.domain.response.ResponseCode;
import cn.aijiamuyingfang.commons.domain.user.UserMessage;
import cn.aijiamuyingfang.commons.domain.user.UserMessageType;
import cn.aijiamuyingfang.weapp.manager.R;
import cn.aijiamuyingfang.weapp.manager.access.server.impl.UserMessageControllerClient;
import cn.aijiamuyingfang.weapp.manager.access.server.utils.RxJavaUtils;
import cn.aijiamuyingfang.weapp.manager.commons.CommonApp;
import cn.aijiamuyingfang.weapp.manager.commons.Constant;
import cn.aijiamuyingfang.weapp.manager.commons.activity.BaseActivity;
import cn.aijiamuyingfang.weapp.manager.commons.utils.DateUtils;
import cn.aijiamuyingfang.weapp.manager.widgets.ClearEditText;
import cn.aijiamuyingfang.weapp.manager.widgets.WeToolBar;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class MessageActionActivity extends BaseActivity {
    private static final String TAG = MessageActionActivity.class.getName();
    @BindView(R.id.toolbar)
    WeToolBar mToolBar;
    @BindView(R.id.message_title)
    ClearEditText mTitleEditText;
    @BindView(R.id.message_roundup)
    ClearEditText mRoundupEditText;
    @BindView(R.id.message_content)
    ClearEditText mContentEditText;
    @BindView(R.id.message_startTime)
    TextView mStartTimeTextView;
    @BindView(R.id.message_finishTime)
    EditText mFinishTimeEditText;
    @BindView(R.id.message_type)
    Spinner mTypeSpinner;
    @BindView(R.id.save_message)
    Button mSaveButton;

    private Calendar mFinishCalendar;
    private CustomDatePickerDialogFragment mFinishTimeDialog;
    private UserMessage mCurMessage;
    private UserMessageControllerApi userMessageControllerApi = new UserMessageControllerClient();
    private List<Disposable> userDisposableList = new ArrayList<>();

    @Override
    protected void init() {
        initData();
        initSpinnerAdapter();
    }


    private void initData() {
        Intent intent = getIntent();
        mCurMessage = intent.getParcelableExtra(Constant.INTENT_MESSAGE);
        if (mCurMessage != null) {
            mToolBar.setRightButtonText("删除");
            mToolBar.setRightButtonOnClickListener(v -> {
                mToolBar.getRightButton().setClickable(false);
                userMessageControllerApi.deleteMessage(CommonApp.getApplication().getUserToken(), CommonApp.getApplication().getUserId(), mCurMessage.getId()).subscribe(new Observer<ResponseBean<Void>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        userDisposableList.add(d);
                    }

                    @Override
                    public void onNext(ResponseBean<Void> responseBean) {
                        if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                            MessageActionActivity.this.finish();
                        } else {
                            Log.e(TAG, responseBean.getMsg());
                        }
                        mToolBar.getRightButton().setClickable(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "delete message failed", e);
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "delete message complete");
                    }
                });
            });

            mTitleEditText.setText(mCurMessage.getTitle());
            mRoundupEditText.setText(mCurMessage.getRoundup());
            mContentEditText.setText(mCurMessage.getContent());
            mStartTimeTextView.setText(DateUtils.date2String(mCurMessage.getCreateTime(), DateUtils.YMD_HMS_FORMAT));
            mFinishCalendar = Calendar.getInstance();
            mFinishCalendar.setTime(mCurMessage.getFinishTime());
            mFinishTimeEditText.setText(DateUtils.date2String(mCurMessage.getFinishTime(), DateUtils.YMD_HMS_FORMAT));
            mTypeSpinner.setSelection(mCurMessage.getType().ordinal());
        } else {
            mStartTimeTextView.setText(DateUtils.date2String(new Date(), DateUtils.YMD_HMS_FORMAT));
            mFinishCalendar = Calendar.getInstance();
        }
    }

    private void initSpinnerAdapter() {
        BaseAdapter mTypeAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return UserMessageType.values().length;
            }

            @Override
            public Object getItem(int position) {
                return UserMessageType.values()[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(MessageActionActivity.this);
                View dropdownView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
                CheckedTextView textView = dropdownView.findViewById(android.R.id.text1);
                textView.setText(UserMessageType.values()[position].name());
                return dropdownView;
            }
        };
        mTypeSpinner.setAdapter(mTypeAdapter);
    }


    @OnClick({R.id.message_finishTime, R.id.save_message})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.message_finishTime:
                showDatePickDlg((EditText) view);
                break;
            case R.id.save_message:
                saveMessage();
                break;
            default:
                break;
        }
    }

    private void saveMessage() {
        mSaveButton.setClickable(false);
        UserMessage userMessage = new UserMessage();
        userMessage.setUserid(CommonApp.getApplication().getUserId());
        userMessage.setTitle(mTitleEditText.getText().toString());
        userMessage.setRoundup(mRoundupEditText.getText().toString());
        userMessage.setContent(mContentEditText.getText().toString());
        userMessage.setReaded(false);
        userMessage.setCreateTime(DateUtils.string2Date(mStartTimeTextView.getText().toString(), DateUtils.YMD_HMS_FORMAT));
        userMessage.setFinishTime(DateUtils.string2Date(mFinishTimeEditText.getText().toString(), DateUtils.YMD_HMS_FORMAT));
        userMessage.setType((UserMessageType) mTypeSpinner.getSelectedItem());
        if (mCurMessage != null) {
            userMessage.setId(mCurMessage.getId());
        }
        userMessageControllerApi.createMessage(CommonApp.getApplication().getUserToken(), CommonApp.getApplication().getUserId(), userMessage).subscribe(new Observer<ResponseBean<UserMessage>>() {
            @Override
            public void onSubscribe(Disposable d) {
                userDisposableList.add(d);
            }

            @Override
            public void onNext(ResponseBean<UserMessage> responseBean) {
                if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                    MessageActionActivity.this.finish();
                } else {
                    Log.e(TAG, responseBean.getMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "create message failed", e);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "create message complete");
                mSaveButton.setClickable(true);
            }
        });
    }

    @OnFocusChange({R.id.message_finishTime})
    public void onFocusChange(View view, boolean hasFocus) {
        if (R.id.message_finishTime == view.getId() && hasFocus) {
            showDatePickDlg((EditText) view);
        }
    }

    protected void showDatePickDlg(EditText editText) {
        if (null == mFinishTimeDialog) {
            mFinishTimeDialog = createDatePickerDialog(editText);
        }
        mFinishTimeDialog.show(getFragmentManager(), CustomDatePickerDialogFragment.class.getSimpleName());
    }

    private CustomDatePickerDialogFragment createDatePickerDialog(EditText editText) {
        if (null == mFinishCalendar) {
            mFinishCalendar = Calendar.getInstance();
            mFinishCalendar.setTimeInMillis(System.currentTimeMillis());
            mFinishCalendar.set(Calendar.HOUR_OF_DAY, 0);
            mFinishCalendar.set(Calendar.MINUTE, 0);
            mFinishCalendar.set(Calendar.SECOND, 0);
            mFinishCalendar.set(Calendar.MILLISECOND, 0);
        }


        CustomDatePickerDialogFragment dialogFragment = new CustomDatePickerDialogFragment();
        dialogFragment.setOnSelectedDateListener((year, monthOfYear, dayOfMonth) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(year).append("-");
            monthOfYear += 1;
            if (monthOfYear > 9) {
                sb.append(monthOfYear);
            } else {
                sb.append("0" + monthOfYear);
            }
            sb.append("-");
            if (dayOfMonth > 9) {
                sb.append(dayOfMonth);
            } else {
                sb.append("0" + dayOfMonth);
            }

            editText.setText(sb.toString() + " 00:00:00");
        });

        Bundle bundle = new Bundle();
        bundle.putSerializable(CustomDatePickerDialogFragment.CURRENT_DATE, mFinishCalendar);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    protected int getContentResourceId() {
        return R.layout.activity_message_detail;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxJavaUtils.dispose(userDisposableList);
    }
}