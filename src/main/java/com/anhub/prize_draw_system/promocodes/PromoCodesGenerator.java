package com.anhub.prize_draw_system.promocodes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

@Component
@Slf4j
@RequiredArgsConstructor
public class PromoCodesGenerator implements CommandLineRunner {

    private final CryptoPromoCodeService cryptoPromoCodeService;

    private static final long MASK_30_BITS = (1L << 30) - 1;

    @Override
    public void run(String... args) throws Exception {
        File file = new File("src/main/resources/promo_codes/promo_codes.txt");
        if (file.exists()) {
            log.info("Promo codes file already exists. Skipping generation.");
            return;
        }

        log.info("Generating promo codes and saving to file: {}", file.getAbsolutePath());

        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            long n = random.nextLong(MASK_30_BITS - 1) + 1;
            String promoCode = cryptoPromoCodeService.generateCode(n);
            writer.write(promoCode);
            writer.newLine();
        }

        writer.close();
    }
}
