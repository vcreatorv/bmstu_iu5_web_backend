package com.valer.rip.lab1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.valer.rip.lab1.services.OffersService;

@Controller
@RequestMapping("/offers")
public class OffersController {

    private final OffersService offersService;

    private final int cartSize = 2;

    public OffersController(OffersService offersService) {
        this.offersService = offersService;
    }

    @GetMapping()
    public String getOffers(Model model) {
        model.addAttribute("offers", offersService.getOffers());
        model.addAttribute("cartSize", cartSize);
        return "offers";
    }

    @GetMapping("/{id}")
    public String getOffers(@PathVariable("id") String id, Model model) {
        model.addAttribute("offer", offersService.getOfferById(id));
        return "offer";
    }

    @GetMapping("/search")
    public String searchOffers(@RequestParam("keyword") String keyword, Model model) {
        if (keyword == null || keyword.isEmpty()) {
            model.addAttribute("offers", offersService.getOffers());
        }
        else{
            model.addAttribute("offers", offersService.searchOffers(keyword));
            model.addAttribute("keyword", keyword.toLowerCase());
        }
        model.addAttribute("cartSize", cartSize);
        return "offers";
    }

    // test requests

    // @GetMapping()
    // @ResponseBody
    // public List<Offer> getOffers() {
    //     return offersService.getOffers();
    // }

    // @GetMapping("/{id}")
    // @ResponseBody
    // public Offer getOffers(@PathVariable("id") String id) {
    //     return offersService.getOfferById(id);
    // }
}
