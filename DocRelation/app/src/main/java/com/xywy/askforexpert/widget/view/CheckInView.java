package com.xywy.askforexpert.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;

/**
 * 签到 动画view
 *
 * @author 王鹏
 * @2015-8-6上午10:24:22
 */
public class CheckInView extends View {

    public String text;
    public String text_new;
    /**
     * 屏幕的高度和宽度
     */
    int view_height = 0;
    int view_width = 0;
    float text_x;
    float text_y;
    private int x1;
    private RefreshHandler mRedrawHandler = new RefreshHandler();

    public CheckInView(Context context, float text_x, float text_y, String text, String text_new) {
        super(context);
        this.text_x = text_x;
        this.text_y = text_y;
        this.text = text;
        this.text_new = text_new;
        mRedrawHandler.sleep(30);
        // TODO Auto-generated constructor stub
        getViewSize(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        view_height = h;
        view_width = w;
    }

    // 获取屏幕的分辨率
    private void getViewSize(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        this.view_height = metrics.heightPixels;
        this.view_width = metrics.widthPixels;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        drawMove(canvas);
    }

    /**
     * 画字体移动
     *
     * @param canvas
     */

    public void drawMove(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTextSize(DensityUtils.sp2px(15));

        paint.setColor(getResources().getColor(R.color.check_in_num));
        float x = view_width * 0.5f;
        float y = view_height * 0.5f;

//		YMApplication.Trace("高度" + text_y + "text_x" + text_x + "  x" + x
//				+ "y." + y + "view_width" + view_width + "view_height"
//				+ view_height);
        if (x1 > 50) {
            paint.setColor(getResources().getColor(R.color.tab_color_nomal));
            canvas.drawText("+" + text_new, text_x, text_y, paint);
        } else {
            canvas.drawText(text, text_x, text_y - 6 * x1, paint);
        }


        x1++;
    }

    class RefreshHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            sleep(20);
            // invalidate();
            postInvalidate();

        }

        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    }

}
