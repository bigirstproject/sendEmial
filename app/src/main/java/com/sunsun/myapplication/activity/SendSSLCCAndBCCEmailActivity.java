package com.sunsun.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sunsun.myapplication.EmailConfiguration;
import com.sunsun.myapplication.LoadingDialog;
import com.sunsun.myapplication.MailSenderInfo;
import com.sunsun.myapplication.PlatformUtils;
import com.sunsun.myapplication.R;
import com.sunsun.myapplication.ScreenShot;
import com.sunsun.myapplication.SimpleMailSender;
import com.sunsun.myapplication.ThreadPoolUtils;
import com.sunsun.myapplication.TitleView;
import com.sunsun.nativelogger.NLogger;
import com.sunsun.nativelogger.logger.base.IFileLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 意见反馈（email）
 *
 * Created by dexian on 4/5/17.
 *
 *  （SSL） 包含抄送和密送
 */

public class SendSSLCCAndBCCEmailActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = SendSSLCCAndBCCEmailActivity.class.getSimpleName();

    public static void invoke(Context context) {
        Intent intent = new Intent(context, SendSSLCCAndBCCEmailActivity.class);
        context.startActivity(intent);
    }

    @Bind(R.id.title_view)
    TitleView titleView;
    @Bind(R.id.from_email)
    EditText fromEmail;
    @Bind(R.id.content)
    EditText content;
    private LoadingDialog loadingDialog;
    private Handler mainHander = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mail_sender_layout);
        ButterKnife.bind(this);
        loadingDialog = LoadingDialog.getInstance(false);
        titleView.setOnLeftClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        titleView.setTitle("意见反馈（SSL(to cc bcc)）");
        titleView.setOnRightClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(fromEmail.getText().toString().trim())) {
            Toast.makeText(this, "请输入邮箱！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!EmailConfiguration.checkEmail(fromEmail.getText().toString().trim())) {
            Toast.makeText(this,"邮箱格式不合法,请检查！", Toast.LENGTH_SHORT).show();
            fromEmail.setEnabled(true);
            return;
        }
        loadingDialog.show(this);
        sendEmail();
    }

    /**
     * send email
     */
    private void sendEmail() {
        final MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setMailServerHost(EmailConfiguration.smtp);
        mailInfo.setMailServerPort(EmailConfiguration.MailServerSSLPort);
        mailInfo.setValidate(true);
        mailInfo.setUserName(EmailConfiguration.userid);
        mailInfo.setPassword(EmailConfiguration.userpsd);
        mailInfo.setFromAddress(EmailConfiguration.from);
        mailInfo.setToAddress(EmailConfiguration.to);
        mailInfo.setToAddresscc(EmailConfiguration.cc);
        mailInfo.setToAddressbcc(EmailConfiguration.bcc);
        mailInfo.setSubject(EmailConfiguration.subject + PlatformUtils.getVersionName(this));
        if (TextUtils.isEmpty(content.getText().toString().trim())) {
            mailInfo.setContent(EmailConfiguration.getEmailBody(SendSSLCCAndBCCEmailActivity.this, fromEmail.getText().toString().trim(), ""));
        } else {
            mailInfo.setContent(EmailConfiguration.getEmailBody(SendSSLCCAndBCCEmailActivity.this, fromEmail.getText().toString().trim(), content.getText().toString().trim()));
        }
        EmailConfiguration.configurationCommandMap();
        NLogger.zipLogs(new IFileLogger.OnZipListener() {
            @Override
            public void onZip(boolean succeed, final String target) {
                NLogger.d(TAG, "result : " + succeed + " ; file path : " + target);
                ThreadPoolUtils.getInstance().execute(new Task(target, mailInfo));
                dissDialog();
            }
        });

    }

    private void dissDialog() {
        mainHander.post(new Runnable() {
            @Override
            public void run() {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    class Task implements Runnable {

        private String target;
        private MailSenderInfo mailInfo;

        public Task(String target, MailSenderInfo mailInfo) {
            this.target = target;
            this.mailInfo = mailInfo;
        }

        @Override
        public void run() {
            try {
                List<String> fileNames = new ArrayList<String>();
                if (!TextUtils.isEmpty(target)) {
                    fileNames.add(target);
                }
                if (ScreenShot.isExistSdCard()) {
                    File sdDir = ScreenShot.getSdCardPath();
                    String dir = sdDir.getAbsolutePath() + File.separator + ScreenShot.PIC_NAME;
                    if (new File(dir).exists()) {
                        fileNames.add(dir);
                    }
                }
                mailInfo.setAttachFileNames(fileNames);
                SimpleMailSender.sendSSLCCAndBCCTextMail(mailInfo);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

}