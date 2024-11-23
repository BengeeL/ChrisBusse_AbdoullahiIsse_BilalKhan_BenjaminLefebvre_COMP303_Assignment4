package bloodbank.bloodbankservice.core.controller;

import bloodbank.bloodbankservice.core.entities.BloodBank;
import bloodbank.bloodbankservice.core.repository.BloodBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/bloodBank")
@RestController
public class BloodBankController {

    @Autowired
    BloodBankRepository bloodBankRepository;

    @RequestMapping("/all")
    public Iterable<BloodBank> getAll() {
        return bloodBankRepository.findAll();
    }

    @RequestMapping("/get")
    public BloodBank getById(@RequestParam long id) {
        return bloodBankRepository.findById(id).orElse(null);
    }

    @RequestMapping("/add")
    public ResponseEntity<String> addBank(
            @RequestParam String BloodBankName,
            @RequestParam String Address,
            @RequestParam String City,
            @RequestParam String Phone,
            @RequestParam String Website,
            @RequestParam String Email
    ) {
        try {
            BloodBank bloodBank = new BloodBank(BloodBankName, Address, City, Phone, Website, Email);
            bloodBankRepository.save(bloodBank);
            return ResponseEntity.ok("Blood Bank added successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add Blood Bank.");
        }
    }


    @RequestMapping("/delete")
    public ResponseEntity<String> deleteBloodBank(@RequestParam long id) {
       try {
           bloodBankRepository.deleteById(id);
           return ResponseEntity.ok("Blood Bank deleted successfully!");
        } catch (Exception e) {
           return ResponseEntity.badRequest().body("Failed to delete Blood Bank.");
        }
    }

    @RequestMapping("/update")
    public ResponseEntity<String> updateBloodBank(
            @RequestParam long id,
            @RequestBody BloodBank bloodBank
    ) {
        try {
            bloodBank.setId(id);
            bloodBankRepository.save(bloodBank);
            return ResponseEntity.ok("Blood Bank updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update Blood Bank.");
        }
    }
}
