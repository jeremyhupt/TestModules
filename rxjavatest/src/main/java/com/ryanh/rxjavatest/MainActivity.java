package com.ryanh.rxjavatest;

import android.view.View;
import android.widget.AdapterView;

import com.ryanh.ryanutils.activity.BaseListActivity;

import rx.functions.Action0;
import rx.functions.Action1;
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;

public class MainActivity extends BaseListActivity {

    @Override
    protected String[] setDatas() {
        String[] str = {"Subject","Subscribing"};
        return str;
    }

    @Override
    protected AdapterView.OnItemClickListener setOnItemClickListener() {
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        subjectTest();
                        break;
                    case 1:
                        break;
                    default:
                        break;
                }
            }
        };
        return listener;
    }

    private void subjectTest() {
        PublishSubject<Integer> publishSubject = PublishSubject.create();
        publishSubject.onNext(1);
        publishSubject.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                println("PublishSubject ", integer);
            }
        });
        publishSubject.onNext(2);
        publishSubject.onNext(3);
        publishSubject.onNext(4);

        println("--------","--------");

        ReplaySubject<Integer> replaySubject = ReplaySubject.create();
        replaySubject.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                println("Early: ", integer);
            }
        });
        replaySubject.onNext(0);
        replaySubject.onNext(1);
        replaySubject.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                println("Late: ", integer);
            }
        });
        replaySubject.onNext(2);

        println("--------","--------");

        BehaviorSubject<Integer> behaviorSubject = BehaviorSubject.create();

        behaviorSubject.onNext(0);
        behaviorSubject.onNext(1);
        behaviorSubject.onNext(2);
        behaviorSubject.onCompleted();

        behaviorSubject.subscribe(
                new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        println("Late: ", integer);
                    }
                },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        println("Error ", throwable.toString());
                    }
                },
                new Action0() {
                    @Override
                    public void call() {
                        println("Completed ", 0);
                    }
                }
        );

        println("--------","--------");

        AsyncSubject<Integer> asyncSubject = AsyncSubject.create();

        asyncSubject.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                println("AsyncSubject: ", integer);
            }
        });
        asyncSubject.onNext(0);
        asyncSubject.onNext(1);
        asyncSubject.onNext(2);
        asyncSubject.onCompleted();

    }

    private void println(String tag, Object o) {
        System.out.println(tag + o);
    }

}
