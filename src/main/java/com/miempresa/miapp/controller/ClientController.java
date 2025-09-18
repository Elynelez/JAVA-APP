package com.miempresa.miapp.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.miempresa.miapp.model.Client;
import com.miempresa.miapp.model.Customer;
import com.miempresa.miapp.model.User;
import com.miempresa.miapp.repository.ClientRepository;
import com.miempresa.miapp.repository.CustomerRepository;
import com.miempresa.miapp.repository.UserRepository;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;

@Controller
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    public byte[] generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    @PostMapping("/client/save")
    public String saveClient(@ModelAttribute Client client) {
        client.setId(client.getId().toUpperCase());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User usuario = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        client.setUserId(usuario);
        clientRepository.save(client);

        return "redirect:/client/download-qr?id=" + client.getId();
    }

    @PostMapping("/customer/save")
    public String saveCustomer(@ModelAttribute Customer customer, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User usuario = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        customer.setUserId(usuario);
        customerRepository.save(customer);

        String link = "http://localhost:8081/delivery/client/form/" + customer.getId();
        model.addAttribute("message", "Pásale el siguiente link a tu cliente:");
        model.addAttribute("link", link);
        model.addAttribute("customer", new Customer());

        return "pages/customer-form";
    }

    @GetMapping("/client/download-qr")
    public ResponseEntity<byte[]> downloadQr(@RequestParam String id) throws WriterException, IOException {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + id));

        String qrContent = "ID: " + client.getId() + "\nNombre: " + client.getName() + "\nTeléfono: "
                + client.getPhone();
        byte[] qrImage = generateQRCodeImage(qrContent, 300, 300);

        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename("cliente_" + client.getId() + ".png")
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "image/png")
                .header("Content-Disposition", contentDisposition.toString())
                .body(qrImage);
    }

    @GetMapping("/check-client/{id}")
    public ResponseEntity<Boolean> checkClient(@PathVariable String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User usuario = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        boolean exists = clientRepository.existsByIdAndUserId(id, usuario);

        return ResponseEntity.ok(exists);
    }

    @GetMapping("/delivery/client/table")
    public String client_table(Model model) {
        List<Client> clients = clientRepository.findAll();
        model.addAttribute("clients", clients);
        return "pages/client-table";
    }

    @GetMapping("/delivery/client/form/{id}")
    public String client_form_with_id(@PathVariable Long id, Model model) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + id));

        model.addAttribute("client", new Client());
        return "pages/client-form";
    }

    @GetMapping("/delivery/customer/form")
    public String customer_form(Model model) {
        model.addAttribute("customer", new Customer());
        return "pages/customer-form";
    }
}
