import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Hello {
    public static <R> Flowable<R> create(final R r) {
        return Flowable.create(emitter -> {
            try {
                emitter.onNext(r);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }, BackpressureStrategy.BUFFER);
    }

    public static void main(String[] args) {
        Integer status = 1636313072;

        List<PageProgress> listProgress = new ArrayList<>();
        listProgress.add(new PageProgress(5,5));
        listProgress.add(new PageProgress(5,5));
        listProgress.add(new PageProgress(5,5));
       System.out.println(PageProgress.getProgress(listProgress));
       System.out.println(Integer.MAX_VALUE);
    }

    private static void getPrinter() {
        Flowable.merge(
                getPortMacInfoApi("192.168.3.181", 9100, 1000).subscribeOn(Schedulers.io()),
                getPortMacInfoApi("192.168.3.188", 9100, 3000).subscribeOn(Schedulers.io()))
                /*.observeOn(Schedulers.computation())
                .subscribe(new Consumer<PortMacInfo>() {
                    @Override
                    public void accept(PortMacInfo o) throws Exception {
                        System.out.println(o);
                    }
                }, throwable -> {
                    System.out.println(throwable);
                })*/
                .toList()
                .map(result -> {
                    List<PortMacInfo> dataList = new ArrayList<>();
                    for (PortMacInfo eachItem : result) {
                        if (eachItem.isAlive)
                            dataList.add(eachItem);
                    }
                    return dataList;
                })
                .toFlowable()
                .blockingSubscribe(model -> {
                    System.out.println(model);
                    System.out.println(Thread.currentThread().getName());
                }, throwable -> {
                    System.out.println(throwable);
                });
    }

    public static boolean fetchNetworkPrinter() {
        String localIp = "192.168.3.112";
        String networkAddressBits = localIp.replaceAll("^(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)$", "$1.$2.$3.");
        List<String> dstIpList = new ArrayList<>();
        for (int i = 2; i < 255; i++) {
            dstIpList.add(networkAddressBits + i);
        }
        long start = System.currentTimeMillis();
        AtomicInteger a = new AtomicInteger(0);
        ApiCompose.create(() -> dstIpList)
                .concatMap(Flowable::fromIterable)
                .concatMap(model -> {
                    System.out.println("start " + model);
                    return getPortMacInfoApi(model, 9100, 100)
                            .subscribeOn(Schedulers.io());
                })
                .toList()
                .map(result -> {
                    List<PortMacInfo> dataList = new ArrayList<>();
                    for (PortMacInfo eachItem : result) {
                        if (eachItem.isAlive)
                            dataList.add(eachItem);
                    }
                    return dataList;
                })
                .toFlowable()
                .blockingSubscribe(model -> {
                    System.out.println("cost " + (System.currentTimeMillis() - start) / 1000);
                    System.out.println(model);
                    System.out.println(Thread.currentThread().getName());
                }, throwable -> {
                    System.out.println(throwable);
                });

        return false;
    }


    public static boolean fetchNetworkPrinter1() {
        String localIp = "192.168.3.112";
        String networkAddressBits = localIp.replaceAll("^(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)$", "$1.$2.$3.");
        List<Flowable<PortMacInfo>> apiList = new ArrayList<>();
        for (int i = 2; i < 255; i++) {
            String dstIp = networkAddressBits + i;
            apiList.add(getPortMacInfoApi(dstIp, 9100, 100).subscribeOn(Schedulers.io()));
        }
        long start = System.currentTimeMillis();
        Flowable.merge(apiList)
                .toList()
                .map(result -> {
                    List<PortMacInfo> dataList = new ArrayList<>();
                    for (PortMacInfo eachItem : result) {
                        if (eachItem.isAlive)
                            dataList.add(eachItem);
                    }
                    return dataList;
                })
                .toFlowable()
                .observeOn(Schedulers.single())
                .subscribe(model -> {
                    System.out.println("cost " + (System.currentTimeMillis() - start) / 1000);
                    System.out.println(model);
                    System.out.println(Thread.currentThread().getName());
                }, throwable -> {
                    System.out.println(throwable);
                });

        return false;
    }

    public static boolean fetchNetworkPrinter2() {
        String localIp = "192.168.3.112";
        String networkAddressBits = localIp.replaceAll("^(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)$", "$1.$2.$3.");
        List<String> dstIpList = new ArrayList<>();
        for (int i = 2; i < 255; i++) {
            dstIpList.add(networkAddressBits + i);
        }
        long start = System.currentTimeMillis();
        AtomicInteger a = new AtomicInteger(0);
        ApiCompose.create(() -> dstIpList)
                .concatMap(Flowable::fromIterable)
                .concatMap(model -> {
                    return getPortMacInfoApi(model, 9100, 100)
                            .subscribeOn(Schedulers.io());
                })
                .toList()
                .map(result -> {
                    List<PortMacInfo> dataList = new ArrayList<>();
                    for (PortMacInfo eachItem : result) {
                        if (eachItem.isAlive)
                            dataList.add(eachItem);
                    }
                    return dataList;
                })
                .toFlowable()
                .blockingSubscribe(model -> {
                    System.out.println("cost " + (System.currentTimeMillis() - start) / 1000);
                    System.out.println(model);
                    System.out.println(Thread.currentThread().getName());
                }, throwable -> {
                    System.out.println(throwable);
                });

        return false;
    }


    public static boolean fetchNetworkPrinter3() {
        String localIp = "192.168.3.112";
        String networkAddressBits = localIp.replaceAll("^(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)$", "$1.$2.$3.");
        List<Flowable<PortMacInfo>> apiList = new ArrayList<>();
        for (int i = 2; i < 255; i++) {
            String dstIp = networkAddressBits + i;
            apiList.add(getPortMacInfoApi(dstIp, 9100, 100));
        }
        long start = System.currentTimeMillis();

        Flowable
                .zip(apiList, new Function<Object[], List<PortMacInfo>>() {
                    @Override
                    public List<PortMacInfo> apply(Object[] o) throws Exception {
                        List<PortMacInfo> dataList = new ArrayList<>();
                        for (Object i : o) {
                            if (i instanceof PortMacInfo && ((PortMacInfo) i).isAlive) {
                                dataList.add((PortMacInfo) i);
                            }
                        }
                        return dataList;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
               .subscribe(model -> {
                    System.out.println("cost " + (System.currentTimeMillis() - start) / 1000);
                    System.out.println(model);
                    System.out.println(Thread.currentThread().getName());
                }, throwable -> {
                    System.out.println(throwable);
                });

        return false;
    }

    private static boolean checkKeyBoardInputLegal(String text) {
        String baseRegex = "^[a-z#]+[0-9\\.半]*";
        return text.matches(baseRegex);
    }

    static String round(double a) {
        DecimalFormat df = new DecimalFormat("###.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(a);
    }

    static String round1(double a) {
        DecimalFormat df = new DecimalFormat("###.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(a);
    }

    static String round4(String str) {
        BigDecimal bigDecimal = new BigDecimal(str);
        DecimalFormat df = new DecimalFormat("###.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(bigDecimal);
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, 10);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }


    /**
     * 获取主机端口信息
     *
     * @param hostName 主机名称
     * @param port     端口
     * @param timeout  超时设置，单位毫秒
     * @return boolean - true/false
     */
    public static PortMacInfo getPortMacInfo(String hostName, int port, int timeout) {
        PortMacInfo portMacInfo = new PortMacInfo(hostName, port);
        SocketAddress socketAddress = new InetSocketAddress(hostName, port);
        Socket socket = new Socket();
        try {
            socket.connect(socketAddress, timeout);
            socket.close();
            portMacInfo.isAlive = true;
            portMacInfo.port = port;
        } catch (IOException exception) {

        }
        return portMacInfo;
    }

    public static Flowable<PortMacInfo> getPortMacInfoApi(String hostName, int port, int timeout) {
        return ApiCompose.create(() -> getPortMacInfo(hostName, port, timeout));
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
