/*
 * Copyright (c) 2016 lhyz Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.lhyz.android.zhihu.daily;

import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import io.lhyz.android.zhihu.daily.base.DefaultSubscriber;
import io.lhyz.android.zhihu.daily.data.bean.StartImage;
import io.lhyz.android.zhihu.daily.data.source.DailyService;
import io.lhyz.android.zhihu.daily.data.source.ServiceCreator;
import io.lhyz.android.zhihu.daily.ui.BaseActivity;
import io.lhyz.android.zhihu.daily.util.NetworkHelper;
import io.lhyz.android.zhihu.daily.util.TagHelper;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * hello,android
 * Created by lhyz on 2016/8/19.
 */
public class AppStart extends BaseActivity {

    private static final String TAG = TagHelper.from(AppStart.class);

    @BindView(R.id.img_start)
    SimpleDraweeView mSimpleDraweeView;
    @BindView(R.id.tv_author)
    TextView mTextView;

    Subscription mSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCountDownTimer.start();

        if (!NetworkHelper.isConnected()) {
            mSubscription = Observable.create(new Observable.OnSubscribe<StartImage>() {
                @Override
                public void call(Subscriber<? super StartImage> subscriber) {
                    if (AppConfig.getInstance().readStartImage() == null) {
                        subscriber.onError(new IllegalStateException("no start image cached"));
                    } else {
                        subscriber.onNext(AppConfig.getInstance().readStartImage());
                    }
                }
            }).subscribe(mStartImageSubscriber);
        } else {
            int width = getResources().getDisplayMetrics().widthPixels;
            String size;
            if (width < 320) {
                size = "320*432";
            } else if (width < 480) {
                size = "480*728";
            } else if (width < 720) {
                size = "720*1184";
            } else {
                size = "1080*1776";
            }

            DailyService service = ServiceCreator.getInstance().createService();
            mSubscription = service.getStartImage(size)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mStartImageSubscriber);
        }
    }

    private final Subscriber<StartImage> mStartImageSubscriber = new DefaultSubscriber<StartImage>() {
        @Override
        protected void onSuccess(StartImage result) {
            if (result == null) {
                onError(new IllegalStateException("No Image Found"));
                return;
            }
            mSimpleDraweeView.setImageURI(Uri.parse(result.getImg()));
            mTextView.setText(result.getText());
            AppConfig.getInstance().saveStartImage(result);
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e.getMessage());
            Snackbar.make(mSimpleDraweeView, "Error when loading image",
                    Snackbar.LENGTH_SHORT).show();
        }
    };

    private final CountDownTimer mCountDownTimer = new CountDownTimer(3000, 100) {
        @Override
        public void onTick(long l) {
            //ignore
        }

        @Override
        public void onFinish() {
            Navigator.navigateToMainActivity(getActivity());
            finish();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (mSubscription != null) {
            if (mSubscription.isUnsubscribed()) {
                mSubscription.unsubscribe();
            }
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.act_start;
    }

    @Override
    protected void setWindowFeature() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
