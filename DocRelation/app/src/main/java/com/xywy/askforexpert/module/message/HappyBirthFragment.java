package com.xywy.askforexpert.module.message;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.interfaces.TextWatcherImpl;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.appcommon.utils.others.WeakHandler;
import com.xywy.askforexpert.model.birthCard.BirthCard;
import com.xywy.askforexpert.module.doctorcircle.DoctorCircleSendMessageActivty;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 生日卡片
 */
public class HappyBirthFragment extends DialogFragment implements View.OnClickListener {
    private final static String LOG_TAG = HappyBirthFragment.class.getSimpleName();
    private static final int REQUEST_CODE = 996;

    private static final int MAX_INPUT_COUNT = 45;

    private static final String MODIFIED_TIME = "modifiedTime";
    private static final String IS_PRESENT_GET = "isPresentGet";
    private static final String IS_CARD_GENERATE = "isCardGenerate";
    private static final String MY_WISHES_SAVED = "myWishes";

    private EditText writeBirthWishes;
    private View view;
    private ImageView close;
    private TextView saveCard;
    private TextView saveAndShare;
    private TextView backToChange;
    private LinearLayout birth_card_save_layout;
    private TextView getBirthPresent;
    private TextView myWishesToSave;
    private LinearLayout wishesInputLayout;
    private SharedPreferences isPresentGet;
    private String uuid;
    private int currentYear;
    private int lastModifiedTime;
    private String imgUrl;

    public HappyBirthFragment() {
        // Required empty public constructor
    }

    public static HappyBirthFragment newInstance() {
        return new HappyBirthFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!YMUserService.isGuest()) {
            uuid = YMApplication.getPID();
        } else {
            uuid = "0";
        }

        // 获取当前年份，用于判断是否为新的一年的生日
        Calendar calendar = new GregorianCalendar();
        currentYear = calendar.get(Calendar.YEAR);
        isPresentGet = getActivity().getSharedPreferences("IsPresentGet", Context.MODE_PRIVATE);
        lastModifiedTime = isPresentGet.getInt(MODIFIED_TIME + uuid, 1970);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_happy_birth, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getBirthPresent = (TextView) view.findViewById(R.id.get_birth_present);
        writeBirthWishes = (EditText) view.findViewById(R.id.birth_wishes_input);
        TextView generateBirthCard = (TextView) view.findViewById(R.id.generate_birth_card);
        backToChange = (TextView) view.findViewById(R.id.back_to_change);
        saveCard = (TextView) view.findViewById(R.id.save_to_sdcard);
        saveAndShare = (TextView) view.findViewById(R.id.save_and_share);
        close = (ImageView) view.findViewById(R.id.close);
        wishesInputLayout = (LinearLayout) view.findViewById(R.id.wishes_input_layout);
        birth_card_save_layout = (LinearLayout) view.findViewById(R.id.birth_card_save_layout);
        myWishesToSave = (TextView) view.findViewById(R.id.my_wishes_to_save);

        if (lastModifiedTime == currentYear) { // 相同年份，不同年份则为默认处理（请求接口)
            if (isPresentGet.getBoolean(IS_PRESENT_GET + uuid, false)) {
                getBirthPresent.setVisibility(View.GONE);
                if (isPresentGet.getBoolean(IS_CARD_GENERATE + uuid, false)) {
                    wishesInputLayout.setVisibility(View.GONE);
                    birth_card_save_layout.setVisibility(View.VISIBLE);
                    String mySavedWishes = isPresentGet.getString(MY_WISHES_SAVED + uuid,
                            getActivity().getResources().getString(R.string.birthday_wishes));
                    myWishesToSave.setText(mySavedWishes);
                } else {
                    Toast.makeText(getActivity(), "您已领取过积分", Toast.LENGTH_SHORT).show();
                    wishesInputLayout.setVisibility(View.VISIBLE);
                }
            }
        } else {
            // 不同年份则清空上一年数据
            isPresentGet.edit().clear().apply();
        }

        getBirthPresent.setOnClickListener(this);
        generateBirthCard.setOnClickListener(this);
        backToChange.setOnClickListener(this);
        saveCard.setOnClickListener(this);
        saveAndShare.setOnClickListener(this);
        close.setOnClickListener(this);

        writeBirthWishes.setOnClickListener(this);
        writeBirthWishes.requestFocus();
        writeBirthWishes.setSelection(writeBirthWishes.getText().length());
        writeBirthWishes.addTextChangedListener(new TextWatcherImpl() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (s.length() > MAX_INPUT_COUNT) {
                    Toast.makeText(getActivity(), "限制最多45字，已超出" + (s.length() - MAX_INPUT_COUNT) + "字",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_birth_present:
                // 获取积分
                StatisticalTools.eventCount(getActivity(), "BirthdayIntegral");
                getBirthPresent();
                break;

            case R.id.generate_birth_card:
                StatisticalTools.eventCount(getActivity(), "BirthdayCard");
                String myBirthWishes = writeBirthWishes.getText().toString().trim();
                if (myBirthWishes.length() > 0 && myBirthWishes.length() <= MAX_INPUT_COUNT) {
                    // 标记是否生成过卡片
                    isPresentGet.edit().putBoolean(IS_CARD_GENERATE + uuid, true).apply();
                    // 保存填写的愿望
                    isPresentGet.edit().putString(MY_WISHES_SAVED + uuid, myBirthWishes).apply();
                    myWishesToSave.setText(myBirthWishes);
                    wishesInputLayout.setVisibility(View.GONE);
                    birth_card_save_layout.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
                } else {
                    if (myBirthWishes.length() > MAX_INPUT_COUNT) {
                        Toast.makeText(getActivity(), "限制最多45字，已超出" + (myBirthWishes.length() - MAX_INPUT_COUNT) + "字",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "请输入你的生日愿望", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.back_to_change:
                birth_card_save_layout.setVisibility(View.GONE);
                wishesInputLayout.setVisibility(View.VISIBLE);
                String mySavedWishes = isPresentGet.getString(MY_WISHES_SAVED + uuid,
                        getActivity().getResources().getString(R.string.birthday_wishes));
                writeBirthWishes.setText(mySavedWishes);
                writeBirthWishes.setSelection(mySavedWishes.length());
                break;

            case R.id.save_to_sdcard:
                StatisticalTools.eventCount(getActivity(), "BirthdayPreservation");
                // android 6.0+ 检查读写内存卡权限并处理
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(getActivity(),
                                    Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), "无法保存，请授予读写内存卡(Storage)权限",
                                Toast.LENGTH_SHORT).show();
                        // 转跳到app详细信息页面，进行授权操作
                        startActivityForResult(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:" + getActivity().getPackageName())), REQUEST_CODE);
                    } else {
                        save2SDCard();
                    }
                } else {
                    save2SDCard();
                }
                break;

            case R.id.save_and_share:
                StatisticalTools.eventCount(getActivity(), "BirthdayShare");
                // android 6.0+ 检查读写内存卡权限并处理
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(getActivity(),
                                    Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                        CommonUtils.permissionRequestDialog(getActivity(), "无法保存，请授予读写内存卡(Storage)权限",
                                REQUEST_CODE);
                    } else {
                        saveAndShare();
                    }
                } else {
                    saveAndShare();
                }
                break;

            case R.id.birth_wishes_input:
                if (writeBirthWishes.getText().toString()
                        .equals(getActivity().getResources().getString(R.string.birthday_wishes))) {
                    writeBirthWishes.setText("");
                }
                break;

            case R.id.close:
                if (getBirthPresent.getVisibility() == View.VISIBLE) {
                    // 领取积分关闭统计
                    StatisticalTools.eventCount(getActivity(), "BirthdayClose");
                } else if (wishesInputLayout.getVisibility() == View.VISIBLE) {
                    // 生成卡片关闭统计
                    StatisticalTools.eventCount(getActivity(), "BirthdayCloseCard");
                } else if (birth_card_save_layout.getVisibility() == View.VISIBLE) {
                    // 保存卡片关闭统计
                    StatisticalTools.eventCount(getActivity(), "BirthdayPreservationClose");
                }
                getDialog().dismiss();
                break;

            default:
                break;
        }
    }

    private void save2SDCard() {
        close.setVisibility(View.GONE);
        backToChange.setVisibility(View.GONE);
        saveCard.setVisibility(View.GONE);
        saveAndShare.setVisibility(View.GONE);
        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String bmpName = String.valueOf(System.currentTimeMillis());
                saveBitmap(view2Bitmap(view), bmpName);
            }
        }, 300);
    }

    private void saveAndShare() {
        close.setVisibility(View.GONE);
        backToChange.setVisibility(View.GONE);
        saveCard.setVisibility(View.GONE);
        saveAndShare.setVisibility(View.GONE);
        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String bmpName = String.valueOf(System.currentTimeMillis());
                boolean b = saveBitmap(view2Bitmap(view), bmpName);
                if (b) {
                    Intent intent = new Intent(getActivity(), DoctorCircleSendMessageActivty.class);
                    intent.putExtra("type", "1");
                    intent.putExtra("birthWishes", "#今天我生日#");
                    intent.putExtra("img_path", getRealPathFromURI(Uri.parse(imgUrl)));
                    startActivity(intent);
                }
            }
        }, 300);
    }

    /**
     * 生成生日卡片
     */
    private Bitmap view2Bitmap(View view) {
        if (view != null) {
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                    Bitmap.Config.ARGB_8888);
            // 获取当前屏幕密度
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int screenDensity = metrics.densityDpi;
            // 将屏幕密度设置给bitmap
            bitmap.setDensity(screenDensity);
            DLog.d(LOG_TAG, "screenDensity = " + screenDensity + ", bitmapDensity = " + bitmap.getDensity());
            //利用bitmap生成画布
            Canvas canvas = new Canvas(bitmap);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            //把view中的内容绘制在画布上
            view.draw(canvas);

            return bitmap;
        }

        return null;
    }

    /**
     * 保存生日卡片
     */
    private boolean saveBitmap(Bitmap bitmap, String bmpName) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (bitmap != null) {
                // 将图片保存到系统图库
                imgUrl = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                        bitmap, bmpName, "XYWY_birthCard");
                if (imgUrl != null) {
                    Toast.makeText(getActivity(), "保存卡片成功", Toast.LENGTH_SHORT).show();
                    close.setVisibility(View.VISIBLE);
                    return true;
                } else {
                    Toast.makeText(getActivity(), "保存卡片失败", Toast.LENGTH_SHORT).show();
                    close.setVisibility(View.VISIBLE);
                    backToChange.setVisibility(View.VISIBLE);
                    saveCard.setVisibility(View.VISIBLE);
                    saveAndShare.setVisibility(View.VISIBLE);
                    return false;
                }
            } else {
                Toast.makeText(getActivity(), "保存卡片失败", Toast.LENGTH_SHORT).show();
                close.setVisibility(View.VISIBLE);
                backToChange.setVisibility(View.VISIBLE);
                saveCard.setVisibility(View.VISIBLE);
                saveAndShare.setVisibility(View.VISIBLE);
                return false;
            }
        } else {
            Toast.makeText(getActivity(), "保存失败，请插入SD卡", Toast.LENGTH_SHORT).show();
            close.setVisibility(View.VISIBLE);
            backToChange.setVisibility(View.VISIBLE);
            saveCard.setVisibility(View.VISIBLE);
            saveAndShare.setVisibility(View.VISIBLE);
            return false;
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String imgPath;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            imgPath = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            imgPath = cursor.getString(idx);
            cursor.close();
        }
        return imgPath;
    }

    /**
     * 领取积分
     */
    private void getBirthPresent() {
        String sign = MD5Util.MD5(YMApplication.getPID() + Constants.MD5_KEY);
        // http://test.yimai.api.xywy.com/app/1.2/index.interface.php?
        String url = CommonUrl.FOLLOW_LIST + "a=birthday&m=addpoints&userid="
                + YMApplication.getPID() + "&bind=" + YMApplication.getPID() + "&sign=" + sign;
        DLog.d(LOG_TAG, LOG_TAG + ": " + url);
        FinalHttp request = new FinalHttp();
        request.get(url, new AjaxCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);

                DLog.d(LOG_TAG, "birth_present: " + s);

                Gson gson = new Gson();
                BirthCard birthCard = gson.fromJson(s, BirthCard.class);

                if (birthCard.getCode() == 10000) {
                    // 获取积分成功
                    Toast.makeText(getActivity(), birthCard.getMsg(), Toast.LENGTH_SHORT).show();
                    // 标记是否领取过积分
                    isPresentGet.edit().putBoolean(IS_PRESENT_GET + uuid, true).apply();
                    isPresentGet.edit().putInt(MODIFIED_TIME + uuid, currentYear).apply();
                    getBirthPresent.setVisibility(View.GONE);
                    wishesInputLayout.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getActivity(), birthCard.getMsg(), Toast.LENGTH_SHORT).show();
                    getDialog().dismiss();
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

                Toast.makeText(getActivity(), "领取积分失败", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        });
    }

}
