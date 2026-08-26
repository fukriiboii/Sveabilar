package com.sveabilar.api.features.service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sveabilar.api.features.service.dto.ServiceOptionResponse;
import com.sveabilar.api.features.service.service.ServiceCatalogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceCatalogService serviceCatalogService;

    @GetMapping("/services")
    public ResponseEntity<List<ServiceOptionResponse>> getServices() {
        return ResponseEntity.ok(serviceCatalogService.getActiveServices());
    }
}
