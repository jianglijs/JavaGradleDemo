import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class RxDemo {
    public static void main(String[] args) {
        final AtomicLong i = new AtomicLong(Integer.MAX_VALUE * 50L);

        Flowable<String> flowableA = ApiCompose
                .create(() -> {
                    System.out.println(Thread.currentThread().getName() + " flowableA sleep before");
                    Thread.sleep(3000);
                    return "C";
                })
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        System.out.println("flowableA A is subscribe");
                    }
                }).subscribeOn(Schedulers.computation())
                .map(model -> {
                    if (model.equals("A")) {
                        return model;
                    } else {
                        return null;
                    }
                }).onErrorReturn(model -> {
                    return "A";
                });

        Flowable<String> flowableB = Flowable.just("B")
                .map(model -> {
                    System.out.println(Thread.currentThread().getName() + " flowableB sleep before");
                    while (i.get() > 0) {
                        i.decrementAndGet();
                    }
                    System.out.println("flowableB sleep end i = " + i.get());
                    return "B";
                }).subscribeOn(Schedulers.computation());


        Flowable<List<Integer>> flowableC = Flowable.just(1)
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        System.out.println("1 is subscribe");
                    }
                })
                .subscribeOn(Schedulers.computation())
                .map(model -> {
                    return model;
                })
                .concatMap(model -> {
                    List<Integer> list = new ArrayList<>();
                    for (int j = 2; j <= 1; j++) {
                        list.add(j);
                    }
                    System.out.println("in the middle");
                    return Flowable.fromIterable(list)
                            .concatMap(index -> Flowable.just(index)
                                    .doOnSubscribe(new Consumer<Subscription>() {
                                        @Override
                                        public void accept(Subscription subscription) throws Exception {
                                            System.out.println(index + " is subscribe");
                                        }
                                    })
                                    .subscribeOn(Schedulers.computation()));
                })
                .toList()
                .toFlowable();


       /* flowableC
                .blockingSubscribe(model -> {
                    System.out.println(model);
                }, throwable -> {
                    System.out.println(Thread.currentThread().getName() + " throwable");
                    System.out.println(throwable);
                });*/
        Flowable<NumberAction> flowableD = cOrderSync(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(integer + " is consumed");
            }
        });
        flowableD
                .blockingSubscribe(model -> {
                    System.out.println(model);
                }, throwable -> {
                    System.out.println(Thread.currentThread().getName() + " throwable");
                    System.out.println(throwable);
                });
        /*        Flowable.zip(flowableA, flowableB, (s, s2) -> "C")
         *//*        .observeOn(Schedulers.single())*//*
                .blockingSubscribe(model -> {
                    System.out.println(model);
                }, throwable -> {
                    System.out.println(Thread.currentThread().getName() + " throwable");
                    System.out.println(throwable);
                });*/
    }


    public static Flowable<NumberAction> cOrderSync(Consumer<Integer> consumer) {
        NumberAction numberAction = new NumberAction();
        return Flowable.just(1)
                .doOnSubscribe(subscription -> ++numberAction.calledNumber)
                .subscribeOn(Schedulers.computation())
                .map(model -> {
                    ++numberAction.successNumber;
                    if (consumer != null) {
                        consumer.accept(model);
                    }
                    int pageCount = 10;
                    List<Integer> list = new ArrayList<>();
                    for (int i = 2; i <= pageCount; i++) {
                        list.add(i);
                    }
                    return list;
                })
                .concatMap(list -> {
                    return Flowable.just(1).concatWith(Flowable.fromIterable(list)
                            .concatMap(index -> Flowable.just(index)
                                    .map(model -> {
                                        if (model == 3) {
                                            return null;
                                        } else {
                                            return model;
                                        }
                                    })
                                    .doOnSubscribe(subscription -> ++numberAction.calledNumber)
                                    .subscribeOn(Schedulers.computation())
                                    .map(result -> {
                                        ++numberAction.successNumber;
                                        if (consumer != null) {
                                            consumer.accept(result);
                                        }
                                        return index;
                                    })));
                })
                .toList()
                .map(result -> {
                    return numberAction;
                })
                .toFlowable()
                .onErrorReturn(throwable -> {
                    System.out.println(" onErrorReturn throwable");
                    return numberAction;
                });
    }

}
