package com.xywy.base.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.xywy.base.R;

import java.lang.ref.WeakReference;


/**
 *  消息弹框工具，等有时间再将它改造成Builder模式
 */
public class MessageDialog {
	private Dialog mDialog;
	private WeakReference<Context> reference;
	private String message;
	private boolean isShowing;
	private Button okButton;
	private Button cancelButton;
	private View view;
	public MessageDialog(Context context) {
		this(context, R.layout.dialog_message);
	}
	public MessageDialog(Context context, int resId) {
		this(context,LayoutInflater.from(context).inflate(resId,
				null));
	}
	public MessageDialog(Context context, View view) {
		super();
		this.reference=new WeakReference<>(context);
		this.view = view;
		init();
	}



	/**
	 * 初始�?	 *
	 * @author lilw
	 * @CrateTime 2012-11-27
	 */
	private void init() {
		mDialog = new Dialog(reference.get(), R.style.AppDialogTheme);
		WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
		params.width = ViewGroup.LayoutParams.MATCH_PARENT;
		params.horizontalWeight = 1;
		mDialog.setContentView(view, params);
		isShowing = false;

		TextView tv_message = (TextView) view.findViewById(R.id.tv_message);
		tv_message.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//加粗

	}

	public View getView() {
		return view;
	}
/**
 * 设置自定义的view内容
 * @author lilw
 * @CrateTime 2013-1-7
 * @param view 要自定义的内�? * 警告�?
 * 	设置自定义的View后，getButton（）、setButton（）等原有布�?��的view不能使用否则可能会报NnullPointException
 */
	public void setView(View view) {
		WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
		params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.horizontalWeight = 1;
//		params.
		getDialog().getWindow().getAttributes().horizontalWeight = 1;
//		getDialog().addContentView(view, params);
		getDialog().setContentView(view, params);
		this.view = view;
		isShowing = false;
	}
/**
 *  获取弹框实例
 * @author lilw
 * @CrateTime 2013-1-7
 * @return Dialog实例
 */
	public Dialog getDialog() {
		return mDialog;
	}
/**
 *获取消息内容
 * @author lilw
 * @CrateTime 2013-1-7
 * @return Messag内容
 */
	public String getMessage() {
		return message;
	}
/**
 * 设置消息内容
 * @author lilw
 * @CrateTime 2013-1-7
 * @param message 要显示的内容
 */
	public MessageDialog setMessage(String message) {
		this.message = message;
		TextView tv_message = (TextView) view.findViewById(R.id.tv_message);
		tv_message.setText(message);
		return this;
	}
	/**
	 * 设置标题
	 * @param title
	 */
	public MessageDialog setTitle(String title){
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText(title);
		View v_title_separate = (View) view.findViewById(R.id.view_title_separate);
		if (TextUtils.isEmpty(title)) {
			v_title_separate.setVisibility(View.GONE);
			tv_title.setVisibility(View.GONE);
		}else {
			v_title_separate.setVisibility(View.VISIBLE);
			tv_title.setVisibility(View.VISIBLE);
		}
		return this;
	}
/**
 * 获取确定按钮
 * @author lilw
 * @CrateTime 2013-1-7
 * @return button 确定按钮
 */
	public Button getOkButton() {
		if (okButton==null) {
			okButton = (Button) view.findViewById(R.id.btn_ok);
		}
		return okButton;
	}
/**
 * 设置确定按钮文字
 * @author lilw
 * @CrateTime 2013-1-7
 * @param okButtonInfo 确定按钮
 */
	public MessageDialog setOkButton(String okButtonInfo, final OnClickListener listener) {
		getOkButton().setText(okButtonInfo);
		getOkButton().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(MessageDialog.this);
			}
		});
		return this;
//		TextView tv_okButton = (TextView) view
//				.findViewById(R.id.textView_meesagedialog_okInfo);
//		tv_okButton.setText(okButtonInfo);
	}
/**
 * 获取取消按钮
 * @author lilw
 * @CrateTime 2013-1-7
 * @return button 取消按钮
 */
	public Button getCancelButton() {
		if (cancelButton==null) {
			cancelButton = (Button) view
					.findViewById(R.id.btn_cancel);
		}
		return cancelButton;
	}


/**
 * 设置取消按钮文字
 * @author lilw
 * @CrateTime 2013-1-7
 * @param cancelButtonInfo 要显示的文字
 */
	public MessageDialog setCancelButton(String cancelButtonInfo, final OnClickListener listener) {
		getCancelButton().setText(cancelButtonInfo);
		getCancelButton().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(MessageDialog.this);
			}
		});
		getCancelButton().setVisibility(View.VISIBLE);
		return this;
//		TextView tv_okButton = (TextView) view
//				.findViewById(R.id.textView_meesagedialog_cancelInfo);
//		tv_okButton.setText(cancelButtonInfo);
	}
/**
 * 显示弹框�?
 * @author lilw
 * @CrateTime 2013-1-7
 */
	public MessageDialog show() {
		getDialog().show();
		isShowing = true;
		return this;
	}
/**
 * 取消弹框�?
 * @author lilw
 * @CrateTime 2013-1-7
 */
	public void dismiss() {
		if (isShowing) {
			getDialog().dismiss();
		}
		isShowing = false;
	}
/**
 * 判断弹框是否正在显示
 * @author lilw
 * @CrateTime 2013-1-7
 * @return
 */
	public boolean isShowing() {
		return isShowing;
	}
/**
 * 设置弹框是否可以取消（点击弹框外区域和按back按钮取消�? * @author lilw
 * @CrateTime 2013-1-7
 * @param flag true为可以取�?false为不可取�? */
	public MessageDialog setCancelable(boolean flag) {
		getDialog().setCancelable(flag);
		return this;
	}

	public interface OnClickListener {
		void onClick(MessageDialog messageDialog);
	}
}
