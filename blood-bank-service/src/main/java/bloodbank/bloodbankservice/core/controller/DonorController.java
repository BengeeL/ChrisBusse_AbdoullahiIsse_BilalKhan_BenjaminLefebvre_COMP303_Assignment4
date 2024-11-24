package bloodbank.bloodbankservice.core.controller;

import bloodbank.bloodbankservice.core.service.DonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class DonorController {
    @Autowired
    private final DonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    // TODO: Implement Controller logic here.
}
