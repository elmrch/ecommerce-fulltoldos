package com.ecommerce.fulltoldos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Integración con la pasarela de pagos Culqi.
 * Mientras no se configure la llave secreta en Ajustes (pago.culqi.secret_key),
 * crearCargo() lanza IllegalStateException y el checkout usa el flujo de "pago pendiente" por WhatsApp.
 */
@Service
public class

CulqiService {

    @Autowired
    private ConfiguracionService configService;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean estaConfigurado() {
        return !configService.get("pago.culqi.secret_key", "").isBlank();
    }

    public String crearCargo(String token, BigDecimal monto, String email, String descripcion) {
        String secretKey = configService.get("pago.culqi.secret_key", "");
        if (secretKey.isBlank()) {
            throw new IllegalStateException("Culqi no está configurado todavía.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(secretKey);

        // Culqi espera el monto en céntimos
        int centimos = monto.multiply(BigDecimal.valueOf(100)).intValue();
        Map<String, Object> body = Map.of(
                "amount", centimos,
                "currency_code", "PEN",
                "email", email,
                "source_id", token,
                "description", descripcion
        );

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, headers);
        Map<?, ?> resp = restTemplate.postForObject("https://api.culqi.com/v2/charges", req, Map.class);
        return resp != null ? String.valueOf(resp.get("id")) : null;
    }
}