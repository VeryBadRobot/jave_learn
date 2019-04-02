import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;

/**
 * @author <a href   ="mailto:wf2311@163.com">wf2311</a>
 * @since 2019/4/2 13:30.
 */
public class DateFormatTest {

    private static int size = 1000_0000;
    private static final String DATE_PATTERN = "yyyyMMddHHmmss";
    private final static SimpleDateFormat SDF = new SimpleDateFormat(DATE_PATTERN);
    private static final LocalDateTime START = LocalDate.of(1970, 1, 1).atStartOfDay();
    private static Date[] dates;

    private static ThreadLocal<DateFormat> threadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat(DATE_PATTERN));

    @Before
    public void before() {
        dates = new Date[size];
        for (int i = 0; i < dates.length; i++) {
            dates[i] = Date.from(START.plusMinutes(i).atZone(ZoneId.systemDefault()).toInstant());
        }

    }


    public void testSerialFormat() {
        formatSerialAll(DateFormatTest::formatDate, "sdf");
    }

    @Test
    public void testSerialFormatJava8DTF() {
        formatSerialAll(DateFormatTest::formatDateJava8DTF, "java8 dtf");
    }

//    @Test
//    public void testParallelFormat() {
//        formatParallelAll(DateFormatTest::formatDate);
//    }

    @Test
    public void testParallelFormatJava8DTF() {
        formatParallelAll(DateFormatTest::formatDateJava8DTF, "java8 dtf");
    }

    @Test
    public void testParallelFormatWithThreadLocal() {
        formatParallelAll(DateFormatTest::formatWithThreadLocal, "sdf threadlocal");
    }


    private static String formatDate(Date date) {
        return SDF.format(date);
    }


    public static String formatWithThreadLocal(Date date) {
        return threadLocal.get().format(date);
    }


    private static String formatDateJava8DTF(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    /**
     * 串行
     */
    private void formatSerialAll(Function<Date, String> function, String code) {
        long start = System.currentTimeMillis();

        for (Date date : dates) {
            function.apply(date);
        }
        System.out.println(code + " 串行执行耗时：\t" + (System.currentTimeMillis() - start));
    }

    /**
     * 并行
     */
    private void formatParallelAll(Function<Date, String> function, String code) {
        long start = System.currentTimeMillis();

        ForkJoinPool forkJoinPool = new ForkJoinPool(10);
        try {
            forkJoinPool.submit(() -> Arrays.stream(dates).parallel().forEach(function::apply)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(code + " 并行执行耗时：\t" + (System.currentTimeMillis() - start));

    }


}

