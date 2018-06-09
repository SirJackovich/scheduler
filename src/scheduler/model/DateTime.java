package scheduler.model;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

public class DateTime {
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private static final long FIFTEEN_MINUTES = MILLISECONDS.convert(15, MINUTES);
  
  public static boolean inFifteenMinutes(String today, String date) throws ParseException{
    Date startTime = DATE_FORMAT.parse(date);
    Date currentTime = DATE_FORMAT.parse(today);          
    long time = startTime.getTime() - currentTime.getTime();
    return time <= FIFTEEN_MINUTES;
  }
  
  public static String makeDateUTC(String date){
    // get the current timezone
    ZoneId timeZone = ZoneId.systemDefault();
    // turn the string date into a LocalDateTime
    LocalDateTime dateLocalDateTime = LocalDateTime.parse(date, DATE_FORMATTER);
    // turn the LocalDateTime into a ZonedDateTime
    ZonedDateTime dateZonedDateTime = dateLocalDateTime.atZone(timeZone);
    // get the UTC ZonedDateTime
    ZonedDateTime UTCZonedDateTime = dateZonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
    // turn the UTC ZonedDateTime into a UTC LocalDateTime
    LocalDateTime UTCLocalDateTime = UTCZonedDateTime.toLocalDateTime();
    // get the UTC Timestamp of the UTC LocalDateTime
    Timestamp UTCTimestamp = Timestamp.valueOf(UTCLocalDateTime);
    // return the UTC Timestamp in string form
    return UTCTimestamp.toString();
  }
  
  public static String makeDateLocal(String date) throws ParseException{
    // create simpleDateFormat
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // create the UTC timezone
    TimeZone utcZone = TimeZone.getTimeZone("UTC");
    // set the timezone on our simpledateformat
    simpleDateFormat.setTimeZone(utcZone);
    // create a date using the simpleDateFormat
    Date myDate = simpleDateFormat.parse(date);
    // update the timezone in the simpleDateFormat
    simpleDateFormat.setTimeZone(TimeZone.getDefault());
    // use the simpleDateFormat to update the date
    String formattedDate = simpleDateFormat.format(myDate);
    // return the string of the updated date
    return formattedDate;
  }
  
}
