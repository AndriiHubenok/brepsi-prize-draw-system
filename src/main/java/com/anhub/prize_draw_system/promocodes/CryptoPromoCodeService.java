package com.anhub.prize_draw_system.promocodes;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class CryptoPromoCodeService {

    private static final String ALPHABET = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final int SERIAL_CHARS = 6;
    private static final int MAC_CHARS = 4;
    private static final int TOTAL_LENGTH = 10;

    private static final long MASK_30_BITS = (1L << 30) - 1;
    private static final long SCRAMBLE_MULTIPLIER = 712398471L;
    private static final long SCRAMBLE_INVERSE = 899654151L;

    private final byte[] secretKey;

    public CryptoPromoCodeService() {
        Dotenv dotenv = Dotenv.load();
        this.secretKey = dotenv.get("PROMO_SECURITY_HMAC_SECRET").getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Generates a promo code for the given serialId. The code consists of 6 characters for the serial and 4 characters for the HMAC signature, totaling 10 characters. The serialId must be in the range [1, 2^30 - 1].
     */
    public String generateCode(long serialId) {
        if (serialId < 1 || serialId > MASK_30_BITS) {
            throw new IllegalArgumentException("Serial ID must be in the range [1, 2^30 - 1]");
        }

        long scrambledSerial = (serialId * SCRAMBLE_MULTIPLIER) & MASK_30_BITS;
        String serialStr = encodeBase32(scrambledSerial, SERIAL_CHARS);

        int mac20Bits = computeHmac20Bits(serialStr);
        String macStr = encodeBase32(mac20Bits, MAC_CHARS);

        return serialStr + macStr;
    }

    /**
     * Validates a promo code and extracts the original serialId if the code is valid. Returns the serialId if the code is valid, and null otherwise.
     */
    public Long validateAndExtractSerial(String rawCode) {
        if (rawCode == null) return null;

        String code = rawCode.replaceAll("[\\s-]", "").toUpperCase();
        if (code.length() != TOTAL_LENGTH) return null;

        String serialStr = code.substring(0, SERIAL_CHARS);
        String macStr = code.substring(SERIAL_CHARS);

        long scrambledSerial;
        long providedMac;
        try {
            scrambledSerial = decodeBase32(serialStr);
            providedMac = decodeBase32(macStr);
        } catch (IllegalArgumentException e) {
            return null;
        }

        int expectedMac = computeHmac20Bits(serialStr);
        if (providedMac != expectedMac) {
            return null;
        }

        long originalSerial = (scrambledSerial * SCRAMBLE_INVERSE) & MASK_30_BITS;
        return originalSerial;
    }

    private int computeHmac20Bits(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey, "HmacSHA256"));
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            int value = ((hash[0] & 0xFF) << 12) | ((hash[1] & 0xFF) << 4) | ((hash[2] & 0xF0) >>> 4);
            return value & 0xFFFFF;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException("Ошибка вычисления HMAC", e);
        }
    }

    private static String encodeBase32(long val, int length) {
        char[] chars = new char[length];
        for (int i = length - 1; i >= 0; i--) {
            chars[i] = ALPHABET.charAt((int) (val & 0x1F));
            val >>>= 5;
        }
        return new String(chars);
    }

    private static long decodeBase32(String s) {
        long val = 0;
        for (int i = 0; i < s.length(); i++) {
            int idx = ALPHABET.indexOf(s.charAt(i));
            if (idx == -1) throw new IllegalArgumentException();
            val = (val << 5) | idx;
        }
        return val;
    }
}
