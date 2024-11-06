package com.valer.rip.lab1.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.valer.rip.lab1.dto.ProviderDutyDTO;
import com.valer.rip.lab1.models.ProviderDuty;
import com.valer.rip.lab1.services.ProviderDutyService;

@RestController
@RequestMapping("/api/provider-duties")
public class ProviderDutiesController {

    private final ProviderDutyService providerDutyService;

    public ProviderDutiesController(ProviderDutyService providerDutyService) {
        this.providerDutyService = providerDutyService;
    }

    
    // public ResponseEntity<Map<String, Object>> getAllProviderDuties(@RequestParam(required = false) String title) {
    //     return ResponseEntity.status(HttpStatus.OK).body(providerDutyService.getAllProviderDuties(title));
    // }
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProviderDuties(
        @RequestParam(required = false) String title,
        @RequestParam(required = false) Boolean monthlyPayment) {
        return ResponseEntity.status(HttpStatus.OK).body(providerDutyService.getAllProviderDuties(title, monthlyPayment));
    
    }

    @GetMapping("/{dutyID}")
    public ResponseEntity<? extends Object> getProviderDutyById(@PathVariable("dutyID") int dutyID) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(providerDutyService.getProviderDutyById(dutyID));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<? extends Object> createProviderDuty(@ModelAttribute ProviderDuty providerDuty) {
        try {
            // ProviderDuty providerDuty = providerDutyService.createProviderDuty(providerDuty);
            return ResponseEntity.status(HttpStatus.OK).body(providerDutyService.createProviderDuty(providerDuty));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{dutyID}/update")
    public ResponseEntity<?> updateProviderDuty(@PathVariable("dutyID") int dutyID, @ModelAttribute ProviderDutyDTO providerDutyDTO) {
        try {
            ProviderDuty updatedDuty = providerDutyService.updateProviderDuty(dutyID, providerDutyDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedDuty);
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("{dutyID}/delete")
    public ResponseEntity<String> deleteProviderDuty(@PathVariable("dutyID") int dutyID) {
        try{
            providerDutyService.deleteProviderDuty(dutyID);
            return ResponseEntity.status(HttpStatus.OK).body("Услуга " + dutyID + " удалена");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/{dutyID}/add")
    public ResponseEntity<?> addProviderDutyToRequest(@PathVariable("dutyID") int dutyID) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(providerDutyService.addProviderDutyToRequest(dutyID));
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка при добавлении услуги в заявку: " + e.getMessage());
        }
    }

    @PostMapping("/{dutyID}/image")
    public ResponseEntity<String> addImageToProviderDuty(@PathVariable("dutyID") int dutyID, @RequestParam("file") MultipartFile file) {
        try {
            providerDutyService.addImageToProviderDuty(dutyID, file);
            return ResponseEntity.status(HttpStatus.OK).body("Картинка была успешно добавлена/изменена");
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при добавлении/изменении изображения услуги: " + e.getMessage());
        }
    }

    // @DeleteMapping("/{dutyID}/{requestID}/delete")
    // public ResponseEntity<String> deleteProviderDutyFromConnectionRequest(@PathVariable("dutyID") int dutyID, @PathVariable("requestID") int requestID) {
    //     try {
    //         providerDutyService.deleteProviderDutyFromConnectionRequest(dutyID, requestID);
    //         return ResponseEntity.status(HttpStatus.OK).body("Услуга с ID = " + dutyID + " успешно удалена из заявки с ID = " + requestID);
    //     } 
    //     catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при удалении услуги с ID = " + dutyID + "из заявки c ID = " + requestID + ": " + e.getMessage());
    //     }
    // }

    // @PutMapping("/{dutyID}/{requestID}/update-amount")
    // public ResponseEntity<? extends Object> updateAmountInDutyRequest(@PathVariable("dutyID") int dutyID, @PathVariable("requestID") int requestID, @RequestParam("amount") int amount) {
    //     try {
    //         DutyRequest updatedDutyRequest = providerDutyService.updateAmountInDutyRequest(dutyID, requestID, amount);
    //         return ResponseEntity.status(HttpStatus.OK).body(updatedDutyRequest);
    //     } 
    //     catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при изменении поля amount услуги с ID = " + dutyID + "в заявке c ID = " + requestID + ": " + e.getMessage());
    //     }
    // }
}
