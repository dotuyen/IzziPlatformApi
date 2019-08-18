package asia.izzi.member.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneUtils {
    private static final  String PHONE_PARTTERN = "(0[9|3|7|8|5])+([0-9]{8})";
    private static Pattern pattern;
    private static Matcher matcher;
    public static boolean isPhone(String phone) {
        pattern = Pattern.compile(PHONE_PARTTERN);
        matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static String formatPhoneNumber(String phone) {
        // todo: implement
        return phone;
    }
}
