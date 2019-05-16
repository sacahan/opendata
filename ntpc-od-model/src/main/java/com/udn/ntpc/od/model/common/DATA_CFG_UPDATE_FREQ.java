package com.udn.ntpc.od.model.common;

import java.util.Calendar;
import java.util.Date;

/**
 * Cron expression 對照，格式是至少六個時間元素，最多七個時間元素<br>
 * 1. 秒(0-59)<br>
 * 2. 分(0-59)<br>
 * 3. 時(0-23)<br>
 * 4. 日(1-31 或 ?)<br>
 * 5. 月(1-12)<br>
 * 6. 星期 (1-7 或 SUN-SAT 或 ?)<br>
 * 7. 年(1970-2099)<br>
 */
public enum DATA_CFG_UPDATE_FREQ {
    /*
    DAY   ("day",   "0 %s   %s  *   *  ?"), // 置換分、時
    WEEK  ("week",  "0 %s   %s  ?   *  %s"), // 置換分、時、星期
    MONTH ("month", "0 %s   %s  %s  *  ?"), // 置換分、時、日
    YEAR  ("year",  "0 %s   %s  %s  %s ?"), // 置換分、時、日、月
     */
    //               1 2    3     4    5      6
    MIN   ("1min",  "0 */1  *     *    *      ?"),
    MIN_2 ("2min",  "0 */2  *     *    *      ?"),
    MIN_3 ("3min",  "0 */3  *     *    *      ?"),
    MIN_4 ("4min",  "0 */4  *     *    *      ?"),
    MIN_5 ("5min",  "0 */5  *     *    *      ?"),
    MIN_10("10min", "0 */10 *     *    *      ?"),
    MIN_15("15min", "0 */15 *     *    *      ?"),
    MIN_20("20min", "0 */20 *     *    *      ?"),
    MIN_30("30min", "0 */30 *     *    *      ?"),
    HOUR  ("hour",  "0 %MIN */1   *    *      ?"),
    DAY   ("day",   "0 %MIN %HOUR *    *      ?"), // 置換分、時
    WEEK  ("week",  "0 %MIN %HOUR ?    *      %WEEK"), // 置換分、時、星期
    MONTH ("month", "0 %MIN %HOUR %DAY *      ?"), // 置換分、時、日
    YEAR  ("year",  "0 %MIN %HOUR %DAY %MONTH ?"), // 置換分、時、日、月
    ;

    private final String value;
    private final String cronExpression;
    
    private DATA_CFG_UPDATE_FREQ(String value, String cronExpression) {
        this.value = value;
        this.cronExpression = cronExpression;
    }
    
    @Override
    public String toString() {
        return value;
    }
    
    public String getValue() {
        return value;
    }

    public String getCronExpression() {
        return cronExpression;
    }
    
    public String formatCronExpression(Date startTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);

        String min = String.valueOf(calendar.get(Calendar.MINUTE));
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String week = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        
        String cron = this.getCronExpression();
        cron = cron.replaceAll("%MIN", min)
                   .replaceAll("%HOUR", hour)
                   .replaceAll("%DAY", day)
                   .replaceAll("%MONTH", month)
                   .replaceAll("%WEEK", week)
                   .trim()
                   .replaceAll(" +", " ");
        return cron;
    }
    
    public static DATA_CFG_UPDATE_FREQ resovleByValue(String value) {
        for (DATA_CFG_UPDATE_FREQ f: values()) {
            if (f.getValue().toUpperCase().equalsIgnoreCase(value.toUpperCase())) {
                return f;
            }
        }
        throw new EnumConstantNotPresentException(DATA_CFG_UPDATE_FREQ.class, value);
    }
    
}
