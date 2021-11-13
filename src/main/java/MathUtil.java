
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * @author：Jiangli
 * @date：2019/02/21 10:12
 */
public class MathUtil {
    //默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;

    public static boolean equals(Long a, Long b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.intValue() == b.longValue();
    }

    public static boolean equalZero(double f) {
        BigDecimal data1 = new BigDecimal(f);
        BigDecimal zero = new BigDecimal(0d);
        return data1.compareTo(zero) == 0;
    }

    public static double getNegativeValue(double f) {
        if (equalZero(f)) {
            return f;
        }
        return -f;
    }

    public static double float2double(float v) {
        return Double.valueOf(Float.valueOf(v).toString()).doubleValue();
    }

    public static int roundedUp(int totalSize, int pageSize) {
        return (totalSize + pageSize - 1) / pageSize;
    }

    public static String format(DecimalFormat decimalFormat, float v) {
        return decimalFormat.format(float2double(v));
    }

    public static long passLeastTime(long leastTime, long start) {
        long pass = System.currentTimeMillis() - start;
        return pass >= leastTime ? 0 : (leastTime - pass);
    }

    /**
     * 获取String的MD5值
     *
     * @param info 字符串
     * @return 该字符串的MD5值
     */
    public static String getMD5(String info) {
        try {
            //获取 MessageDigest 对象，参数为 MD5 字符串，表示这是一个 MD5 算法（其他还有 SHA1 算法等）：
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            //update(byte[])方法，输入原数据
            //类似StringBuilder对象的append()方法，追加模式，属于一个累计更改的过程
            md5.update(info.getBytes("UTF-8"));
            //digest()被调用后,MessageDigest对象就被重置，即不能连续再次调用该方法计算原数据的MD5值。可以手动调用reset()方法重置输入源。
            //digest()返回值16位长度的哈希值，由byte[]承接
            byte[] md5Array = md5.digest();
            //byte[]通常我们会转化为十六进制的32位长度的字符串来使用,本文会介绍三种常用的转换方法
            return bytesToHex1(md5Array);
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    private static String bytesToHex1(byte[] md5Array) {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < md5Array.length; i++) {
            int temp = 0xff & md5Array[i];//TODO:此处为什么添加 0xff & ？
            String hexString = Integer.toHexString(temp);
            if (hexString.length() == 1) {//如果是十六进制的0f，默认只显示f，此时要补上0
                strBuilder.append("0").append(hexString);
            } else {
                strBuilder.append(hexString);
            }
        }
        return strBuilder.toString();
    }

    //通过java提供的BigInteger 完成byte->HexString
    private static String bytesToHex2(byte[] md5Array) {
        BigInteger bigInt = new BigInteger(1, md5Array);
        return bigInt.toString(16);
    }

    //通过位运算 将字节数组到十六进制的转换
    public static String bytesToHex3(byte[] byteArray) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] resultCharArray = new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        return new String(resultCharArray);
    }


    public static String getFileSizeDescription(long size) {
        StringBuffer bytes = new StringBuffer();
        DecimalFormat format = new DecimalFormat("###.0");
        if (size >= 1024 * 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0 * 1024.0));
            bytes.append(format.format(i)).append("GB");
        } else if (size >= 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0));
            bytes.append(format.format(i)).append("MB");
        } else if (size >= 1024) {
            double i = (size / (1024.0));
            bytes.append(format.format(i)).append("KB");
        } else if (size < 1024) {
            if (size <= 0) {
                bytes.append("0B");
            } else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();
    }


    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * Java double乘法丢失
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
     * 提供精确的乘法运算。
     * Java double乘法丢失
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2,double v3) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        BigDecimal b3 = new BigDecimal(Double.toString(v3));
        return b1.multiply(b2).multiply(b3).doubleValue();
    }

    public static long mul(int v1, int v2) {
        BigInteger b1 = new BigInteger(Integer.toString(v1));
        BigInteger b2 = new BigInteger(Integer.toString(v2));
        return b1.multiply(b2).longValue();
    }

    public static int getMax(int[] array) {
        int max = array[0];
        for (int i : array) {
            if (i > max) {
                max = i;
            }
        }
        return max;
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
        return div(v1, v2, DEF_DIV_SCALE);
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

    public static int getLeastTenMultiple(int num) {
        if (num % 10 == 0) {
            return num;
        }
        return (num / 10) * 10 + 10;
    }

    public static int getRandomInt(int bound) {
        Random random = new Random();
        return random.nextInt(bound);
    }

/*    public static String getRandom(int len) {
        Random random = new Random();
        int rs = (int) ((random.nextDouble() * 9 + 1) * Math.pow(10, len - 1));
        return String.valueOf(rs);
    }*/

    public static String getRandom(int length) {
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = '0';
        }
        String patten = new String(chars);
        long randomBase = Long.parseLong("1" + patten);
        long rand = (long) (Math.random() * randomBase);
        DecimalFormat decimalFormat = new DecimalFormat(patten);

        return decimalFormat.format(rand);
    }

    public static String moneyToChinese(BigDecimal i_money) {
        if (i_money.equals(BigDecimal.ZERO)) {
            return "零圆整";
        }
        if (i_money.doubleValue() >= 100000000 || i_money.doubleValue() < 0.01) {
            return "";
        }
        i_money = i_money.setScale(2, RoundingMode.HALF_UP);
        String numStr = i_money.toString();
        int pointPos = numStr.indexOf('.');
        String s_int = null; //整数部分
        String s_point = null; //小数部分
        if (pointPos >= 0) {
            s_int = numStr.substring(0, pointPos);
            s_point = numStr.substring(pointPos + 1);
        } else {
            s_int = numStr;
        }
        StringBuilder sb = new StringBuilder();
        if (!"0".equals(s_int)) {
            int groupCount = (int) Math.ceil(s_int.length() / 4.0);
            for (int group = 0; group < groupCount; group++) {
                boolean zeroFlag = true;
                boolean noZeroFlag = false;
                int start = (s_int.length() % 4 == 0 ? 0 : (s_int.length() % 4 - 4)) + 4 * group;
                for (int i = 0; i < 4; i++) {
                    if (i + start >= 0) {
                        int value = s_int.charAt(i + start) - '0';
                        if (value > 0) {
                            sb.append(CN_UPPER_NUMBER[value]);
                            if (i < 3) {
                                sb.append(CN_UPPER_UNIT[i]);
                            }
                            zeroFlag = true;
                            noZeroFlag = true;
                        } else if (zeroFlag) {
                            sb.append('零');
                            zeroFlag = false;
                        }
                    }
                }
                if (sb.charAt(sb.length() - 1) == '零') {
                    sb.deleteCharAt(sb.length() - 1);
                }
                if (noZeroFlag || groupCount - group == 1) {
                    sb.append(CN_GROUP[groupCount - group - 1]);
                }
            }
        }
        if (s_point == null || "00".equals(s_point)) {
            sb.append('整');
        } else {
            int j = s_point.charAt(0) - '0';
            int f = s_point.charAt(1) - '0';
            if (j > 0) {
                sb.append(CN_UPPER_NUMBER[j]).append('角');
                if (f != 0) {
                    sb.append(CN_UPPER_NUMBER[f]).append('分');
                }
            } else if ("0".equals(s_int)) {
                sb.append(CN_UPPER_NUMBER[f]).append('分');
            } else {
                sb.append('零').append(CN_UPPER_NUMBER[f]).append('分');
            }
        }
        return sb.toString();
    }


    private static final char[] CN_UPPER_NUMBER = "零壹贰叁肆伍陆柒捌玖".toCharArray();
    private static final char[] CN_UPPER_UNIT = "仟佰拾".toCharArray();
    private static final char[] CN_GROUP = "圆万亿".toCharArray();
}
