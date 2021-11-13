import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author：Jiangli
 * @date：2019/01/20 20:50
 */
public class ApiCompose {
    /**
     * 统一线程处理
     * compose简化线程
     */
/*    public static <T> FlowableTransformer<T, T> rxIoSchedulerHelper() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }*/

    /**
     * 统一线程处理
     * compose简化线程
     */
    public static <T> FlowableTransformer<T, T> rxIoAndComputationSchedulerHelper() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.computation());
    }

/*    *//**
     * 统一线程处理
     * compose简化线程
     *//*
    public static <T> FlowableTransformer<T, T> rxComputationScheduleHelper() {
        return observable -> observable.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
    }

    *//**
     * 统一线程处理
     * compose简化线程
     *//*
    public static <T> ObservableTransformer<T, T> rxIoSchedulerObservableHelper() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }*/

    /**
     * 本类用来生成Flowable
     */
    public static <T> Flowable<T> createData(final T t) {
        return Flowable.create(emitter -> {
            try {
                emitter.onNext(t);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }, BackpressureStrategy.BUFFER);
    }

    public static <R> Flowable<R> create(final Function1<R> t) {
        return Flowable.create(emitter -> {
            try {
                R r = t.apply();
                emitter.onNext(r);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }, BackpressureStrategy.BUFFER);
    }

}
