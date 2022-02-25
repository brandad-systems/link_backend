package de.bas.link.utils;

import de.bas.link.domain.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class Utils {
    private static final Tika tika = new Tika();

    public static User getUser() {
        User user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.getPrincipal() != null
                && authentication.getPrincipal() instanceof User) {
            user = (User) authentication.getPrincipal();
        }
        return user;
    }

    public static boolean isImageStream(byte [] prefix) {
        String mediaType = tika.detect(prefix);
        log.info(String.format("File mediatype is '%s'", mediaType));
        return mediaType.startsWith("image");
    }

    public static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i+1), 16));
        }
        return data;
    }
}
