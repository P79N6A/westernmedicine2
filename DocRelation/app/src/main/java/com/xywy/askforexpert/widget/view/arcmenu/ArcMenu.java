/*
 * Copyright (C) 2012 Capricorn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xywy.askforexpert.widget.view.arcmenu;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * A custom view that looks like the menu in <a href="https://path.com">Path
 * 2.0</a> (for iOS).
 * 
 * @author Capricorn
 * 
 */
public class ArcMenu  {
    private ArcLayout mArcLayout;

    private View clickView;

    public ArcMenu(ArcLayout mArcLayout, View clickView) {
        this.mArcLayout = mArcLayout;
        this.clickView = clickView;
        init();
    }


    private void init() {
        clickView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    clickView.startAnimation(createStartButtonAnimation(mArcLayout.isExpanded()));
                    switchState();
                }

                return false;
            }
        });
        mArcLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchState();
            }
        });
    }

    public void switchState(){
        clickView.setSelected(mArcLayout.isExpanded());
        mArcLayout.switchState();
    }

    public void setCenterView(View centerView) {
        mArcLayout.setCenterView(centerView);
    }

    public void addItem(View item, View.OnClickListener listener) {
        mArcLayout.addView(item);
        item.setOnClickListener(getItemClickListener(listener));
    }

    private View.OnClickListener getItemClickListener(final View.OnClickListener listener) {
        return new OnClickListener() {

            @Override
            public void onClick(final View viewClicked) {
//                Animation animation = bindItemAnimation(viewClicked, true, 400);
//                animation.setAnimationListener(new AnimationListener() {
//
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        clickView.postDelayed(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                itemDidDisappear();
//                            }
//                        }, 0);
//                    }
//                });
//
//                final int itemCount = mArcLayout.getChildCount();
//                for (int i = 0; i < itemCount; i++) {
//                    View item = mArcLayout.getChildAt(i);
//                    if (viewClicked != item) {
//                        bindItemAnimation(item, false, 300);
//                    }
//                }
//
//                mArcLayout.invalidate();
//                clickView.startAnimation(createStartButtonAnimation(true));

                mArcLayout.switchState();
                if (listener != null) {
                    listener.onClick(viewClicked);
                }
            }
        };
    }

//    private Animation bindItemAnimation(final View child, final boolean isClicked, final long duration) {
//        Animation animation = createItemDisapperAnimation(duration, isClicked);
//        child.setAnimation(animation);
//
//        return animation;
//    }
//
//    private void itemDidDisappear() {
//        final int itemCount = mArcLayout.getChildCount();
//        for (int i = 0; i < itemCount; i++) {
//            View item = mArcLayout.getChildAt(i);
//            item.clearAnimation();
//        }
//
//        mArcLayout.switchState();
//    }

//    private static Animation createItemDisapperAnimation(final long duration, final boolean isClicked) {
//        AnimationSet animationSet = new AnimationSet(true);
//        animationSet.addAnimation(new ScaleAnimation(1.0f, isClicked ? 2.0f : 0.0f, 1.0f, isClicked ? 2.0f : 0.0f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
//        animationSet.addAnimation(new AlphaAnimation(1.0f, 0.0f));
//
//        animationSet.setDuration(duration);
//        animationSet.setInterpolator(new DecelerateInterpolator());
//        animationSet.setFillAfter(true);
//
//        return animationSet;
//    }

//    private static Animation createStartButtonAnimation(final boolean expanded) {
//        Animation animation = new RotateAnimation(expanded ? 45 : 0, expanded ? 0 : 45, Animation.RELATIVE_TO_SELF,
//                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        animation.setStartOffset(0);
//        animation.setDuration(100);
//        animation.setInterpolator(new DecelerateInterpolator());
//        animation.setFillAfter(true);
//
//        return animation;
//    }
}
