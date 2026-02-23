package kg.zhaparov.telegrambot.service;

import org.springframework.stereotype.Service;

@Service
public class NormalizeService {
    private NormalizeService() {}

    public String normalizeKg(String raw) {
        if (raw == null) return null;
        String s = raw.trim();
        String digits = s.replaceAll("\\D+", "");

        if (digits.startsWith("\\d") && digits.length() == 9) {
            return s;
        }

        if (digits.startsWith("0") && digits.length() == 10) {
            digits = "996" + digits.substring(1);
        }

        if (digits.startsWith("996") && digits.length() == 12) {
            return "+" + digits;
        }

        return digits.isEmpty() ? null : "+" + digits;
    }
}