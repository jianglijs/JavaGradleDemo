
import java.math.BigDecimal;
import java.util.List;

/**
 * @author：Jiangli
 * @date：2021/11/09 11:01
 */
public class PageProgress {
    public Integer current;
    public Integer pageCount;

    public PageProgress() {
    }

    public PageProgress(Integer current, Integer pageCount) {
        this.current = current;
        this.pageCount = pageCount;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public static boolean isProgressing(List<PageProgress> listProgress) {
        for (PageProgress pageProgress : listProgress) {
            if (pageProgress.pageCount != null && pageProgress.current != null && pageProgress.pageCount > 1) {
                return true;
            }
        }
        return false;
    }

    public static int getProgress(List<PageProgress> listProgress) {
        double progress = 0;
        double ratio = MathUtil.div(100, listProgress.size());

        for (PageProgress pageProgress : listProgress) {
            if (pageProgress.pageCount != null && pageProgress.current != null && pageProgress.pageCount > 1) {
                progress = MathUtil.add(progress, MathUtil.mul(MathUtil.div(pageProgress.current, pageProgress.pageCount), ratio));
            }
        }
        return (int) MathUtil.round(progress, 3);
    }
}
