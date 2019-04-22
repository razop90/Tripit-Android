package com.example.TripitAndroid;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Consts {
    public static class Tables {
        public static String PostsTableName = "Posts";
        public static String UserInfoTableName = "Users";
        public static String ImagesFolderName = "ImagesStorage";
        public static String ProfileImagesFolderName = "ProfileImagesStorage";
        public static String LikesTableName = "likes";
        public static String CommentsTableName = "comments";
    }

    public static class General {
        public static String convertTimestampToStringDate(long serverTimestamp, String format) {
            if (format == null)
                format = "dd/MM/yyyy HH:mm";

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(serverTimestamp);
            SimpleDateFormat fmt = new SimpleDateFormat(format, Locale.US);

            return fmt.format(cal.getTime()); //This returns a string formatted in the above way.
        }
    }
}
