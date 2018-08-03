package cn.aijiamuyingfang.weapp.manager.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import cn.aijiamuyingfang.client.rest.api.UserMessageControllerApi;
import cn.aijiamuyingfang.commons.domain.response.ResponseCode;
import cn.aijiamuyingfang.commons.domain.user.UserMessage;
import cn.aijiamuyingfang.commons.domain.user.UserMessageType;
import cn.aijiamuyingfang.weapp.manager.R;
import cn.aijiamuyingfang.weapp.manager.access.server.impl.UserMessageControllerClient;
import cn.aijiamuyingfang.weapp.manager.commons.CommonApp;
import cn.aijiamuyingfang.weapp.manager.commons.Constant;
import cn.aijiamuyingfang.weapp.manager.commons.activity.BaseActivity;
import cn.aijiamuyingfang.weapp.manager.commons.utils.DateUtils;
import cn.aijiamuyingfang.weapp.manager.widgets.ClearEditText;
import cn.aijiamuyingfang.weapp.manager.widgets.WeToolBar;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class MessageActionActivity extends BaseActivity {
    private static final String TAG = "MessageActionActivity";
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
    TextView mFinishTimeTextView;
    @BindView(R.id.message_type)
    Spinner mTypeSpinner;
    @BindView(R.id.message_save)
    Button mSaveButton;

    private Calendar mFinishCalendar;
    private DatePickerDialog mDatePickerDialog;

    private UserMessageControllerApi userMessageControllerApi = new UserMessageControllerClient();

    @Override
    protected void init() {
        initData();
        initSpinnerAdapter();
    }


    private void initData() {
        Intent intent = getIntent();
        UserMessage mMessage = intent.getParcelableExtra(Constant.INTENT_MESSAGE);
        if (mMessage != null) {
            mToolBar.setRightButtonText("删除");
            mToolBar.setRightButtonOnClickListener(v -> {
                mToolBar.getRightButton().setClickable(false);
                userMessageControllerApi.deleteMessage(CommonApp.getApplication().getUserToken(), CommonApp.getApplication().getUserId(), mMessage.getId()).subscribe(responseBean -> {
                    if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                        MessageActionActivity.this.finish();
                    }
                    mToolBar.getRightButton().setClickable(true);
                });
            });

            mTitleEditText.setText(mMessage.getTitle());
            mRoundupEditText.setText(mMessage.getRoundup());
            mContentEditText.setText(mMessage.getContent());
            mStartTimeTextView.setText(DateUtils.date2String(mMessage.getCreateTime(), DateUtils.YMD_HMS_FORMAT));
            mFinishCalendar = Calendar.getInstance();
            mFinishCalendar.setTime(mMessage.getFinishTime());
            mFinishTimeTextView.setText(DateUtils.date2String(mMessage.getFinishTime(), DateUtils.YMD_HMS_FORMAT));
            mTypeSpinner.setSelection(mMessage.getType().ordinal());
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


    @OnClick({R.id.message_finishTime, R.id.message_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.message_finishTime:
                if (null == mDatePickerDialog) {
                    mDatePickerDialog = new DatePickerDialog(MessageActionActivity.this, (v, year, month, dayOfMonth) -> {
                        mFinishCalendar.set(Calendar.YEAR, year);
                        mFinishCalendar.set(Calendar.MONTH, month);
                        mFinishCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        mFinishTimeTextView.setText(DateUtils.date2String(mFinishCalendar.getTime(), DateUtils.YMD_HMS_FORMAT));
                    }, mFinishCalendar.get(Calendar.YEAR), mFinishCalendar.get(Calendar.MONTH),
                            mFinishCalendar.get(Calendar.DAY_OF_MONTH));
                }
                mDatePickerDialog.show();
                break;
            case R.id.message_save:
                mSaveButton.setClickable(false);
                UserMessage userMessage = new UserMessage();
                userMessage.setUserid("system");
                userMessage.setTitle(mTitleEditText.getText().toString());
                userMessage.setRoundup(mRoundupEditText.getText().toString());
                userMessage.setContent(mContentEditText.getText().toString());
                userMessage.setReaded(false);
                userMessage.setCreateTime(new Date());
                userMessage.setFinishTime(mFinishCalendar.getTime());
                userMessage.setType((UserMessageType) mTypeSpinner.getSelectedItem());
                userMessageControllerApi.createMessage(CommonApp.getApplication().getUserToken(), CommonApp.getApplication().getUserId(), userMessage).subscribe(responseBean -> {
                    if (ResponseCode.OK.getCode().equals(responseBean.getCode())) {
                        MessageActionActivity.this.finish();
                    } else {
                        Log.e(TAG, responseBean.getMsg());
                    }
                    mSaveButton.setClickable(true);
                });
                break;
            default:
                break;
        }
    }

    @Override
    protected int getContentResourceId() {
        return R.layout.activity_message_detail;
    }
}