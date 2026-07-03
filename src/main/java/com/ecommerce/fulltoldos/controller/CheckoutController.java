package com.ecommerce.fulltoldos.controller;

import com.ecommerce.fulltoldos.model.Pedido;
import com.ecommerce.fulltoldos.model.PedidoItem;
import com.ecommerce.fulltoldos.repository.PedidoRepository;
import com.ecommerce.fulltoldos.repository.UsuarioRepository;
import com.ecommerce.fulltoldos.service.ConfiguracionService;
import com.ecommerce.fulltoldos.service.CulqiService;
import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CheckoutController {

    @Autowired
    private ConfiguracionService configService;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CulqiService culqiService;

    @GetMapping("/checkout")
    public String checkout(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("wspLink", "https://wa.me/" + configService.get("cotizacion.whatsapp", "51999999999"));
        model.addAttribute("culqiPublicKey", configService.get("pago.culqi.public_key", ""));
        model.addAttribute("costoEnvio", configService.get("pago.envio.costo", "15"));
        model.addAttribute("envioGratisDesde", configService.get("pago.envio.gratis_desde", "300"));

        if (user != null) {
            usuarioRepository.findByEmail(user.getUsername()).ifPresent(u -> {
                model.addAttribute("clienteNombre", u.getNombre());
                model.addAttribute("clienteEmail", u.getEmail());
            });
        }
        return "checkout";
    }

    @PostMapping("/checkout/crear")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> crearPedido(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String email,
            @RequestParam String telefono,
            @RequestParam String direccion,
            @RequestParam(defaultValue = "") String referencia,
            @RequestParam String ciudad,
            @RequestParam(defaultValue = "") String provincia,
            @RequestParam(defaultValue = "") String codigoPostal,
            @RequestParam(defaultValue = "estandar") String metodoEnvio,
            @RequestParam String itemsJson
    ) {
        List<Map<String, Object>> items;
        try {
            items = new ObjectMapper().readValue(itemsJson, List.class);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Carrito inválido."));
        }
        if (items.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Tu carrito está vacío."));
        }

        Pedido pedido = new Pedido();
        pedido.setNombre(nombre);
        pedido.setApellido(apellido);
        pedido.setEmail(email);
        pedido.setTelefono(telefono);
        pedido.setDireccion(direccion);
        pedido.setReferencia(referencia);
        pedido.setCiudad(ciudad);
        pedido.setProvincia(provincia);
        pedido.setCodigoPostal(codigoPostal);
        pedido.setMetodoEnvio(metodoEnvio);

        if (user != null) {
            usuarioRepository.findByEmail(user.getUsername()).ifPresent(pedido::setUsuario);
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        for (Map<String, Object> it : items) {
            PedidoItem item = new PedidoItem();
            item.setPedido(pedido);
            item.setProductoId(it.get("id") != null ? Long.valueOf(String.valueOf(it.get("id"))) : null);
            item.setNombre(String.valueOf(it.get("name")));
            item.setImagenUrl(it.get("img") != null ? String.valueOf(it.get("img")) : null);
            BigDecimal precio = it.get("precio") != null && !"null".equals(String.valueOf(it.get("precio")))
                    ? new BigDecimal(String.valueOf(it.get("precio"))) : BigDecimal.ZERO;
            int cantidad = it.get("qty") != null ? Integer.parseInt(String.valueOf(it.get("qty"))) : 1;
            item.setPrecioUnitario(precio);
            item.setCantidad(cantidad);
            subtotal = subtotal.add(precio.multiply(BigDecimal.valueOf(cantidad)));
            pedido.getItems().add(item);
        }

        BigDecimal costoEnvioBase = new BigDecimal(configService.get("pago.envio.costo", "15"));
        BigDecimal envioGratisDesde = new BigDecimal(configService.get("pago.envio.gratis_desde", "300"));
        BigDecimal costoEnvio = "recojo".equals(metodoEnvio) || subtotal.compareTo(envioGratisDesde) >= 0
                ? BigDecimal.ZERO : costoEnvioBase;

        pedido.setSubtotal(subtotal);
        pedido.setCostoEnvio(costoEnvio);
        pedido.setTotal(subtotal.add(costoEnvio));
        pedido.setEstado(Pedido.Estado.PENDIENTE_PAGO);

        pedidoRepository.save(pedido);

        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("status", "ok");
        resp.put("pedidoId", pedido.getId());
        resp.put("total", pedido.getTotal());
        resp.put("culqiEnabled", culqiService.estaConfigurado());

        if (!culqiService.estaConfigurado()) {
            String wspNum = configService.get("cotizacion.whatsapp", "51999999999");
            StringBuilder sb = new StringBuilder("Hola Full Toldos! Confirmo mi pedido #" + pedido.getId() + ":\n");
            for (PedidoItem it : pedido.getItems()) {
                sb.append("• ").append(it.getNombre()).append(" (x").append(it.getCantidad()).append(")\n");
            }
            sb.append("Total: S/ ").append(pedido.getTotal()).append("\n");
            sb.append("Envío a: ").append(direccion).append(", ").append(ciudad);
            String wspUrl = "https://wa.me/" + wspNum + "?text=" + URLEncoder.encode(sb.toString(), StandardCharsets.UTF_8);
            resp.put("wspUrl", wspUrl);
        }

        return ResponseEntity.ok(resp);
    }

    @PostMapping("/checkout/{id}/pagar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> confirmarPago(@PathVariable Long id, @RequestParam String culqiToken) {
        return pedidoRepository.findById(id).map(pedido -> {
            try {
                String chargeId = culqiService.crearCargo(culqiToken, pedido.getTotal(), pedido.getEmail(),
                        "Pedido #" + pedido.getId() + " - Full Toldos");
                pedido.setCulqiChargeId(chargeId);
                pedido.setEstado(Pedido.Estado.PAGADO);
                pedidoRepository.save(pedido);
                return ResponseEntity.ok(Map.<String, Object>of("status", "ok"));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.<String, Object>of("status", "error", "message", e.getMessage()));
            }
        }).orElse(ResponseEntity.notFound().build());
    }
}