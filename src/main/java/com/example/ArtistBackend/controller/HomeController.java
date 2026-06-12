package com.example.ArtistBackend.controller;

import com.example.ArtistBackend.model.AboutHero;
import com.example.ArtistBackend.model.ArtWork;
import com.example.ArtistBackend.model.Contact;
import com.example.ArtistBackend.model.HomeHero;
import com.example.ArtistBackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    ArtworkService artworkService;
    @Autowired
    HomeHeroService homeHeroService;
    @Autowired
    private AboutHeaderService aboutHeaderService;
    @Autowired
    private GalleryHeaderService galleryHeaderService;
    @Autowired
    private ShopHeaderService shopHeaderService;
    @Autowired
    private ContactHeaderService contactHeaderService;
    @Autowired
    private TestimonialService testimonialService;
    @Autowired
    private AwardService awardService;
    @Autowired
    private StudioService studioService;
    @Autowired
    private ExhibitionService exhibitionService;
    @Autowired
    private FAQService faqService;
    @Autowired
    private ContactService contactService;

    @GetMapping("/")
    public String index(Model model) throws IOException {
    model.addAttribute("heroLists", homeHeroService.getAllHeroes());
    model.addAttribute("testimonials",testimonialService.findAllTestimonial());
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

            return "redirect:/admin";
        }

        return "index";
    }
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("headers",aboutHeaderService.findAllHeader());
        model.addAttribute("awards",awardService.findAllAwards());
        return "about";
    }
//    @GetMapping("/gallery")
//    public String gallery(Model model) throws IOException {
//        List<ArtWork> artWorks = artworkService.getAllArtworks();
//        model.addAttribute("artWorks", artWorks);
//        return "gallery.html";
//    }
@GetMapping("/gallery")
public String gallery(Model model) {
    model.addAttribute("artWorks",
            artworkService.getArtworksPage(0, 9).getContent());
    model.addAttribute("headers",galleryHeaderService.findAllGalleryHeader());
    model.addAttribute("studios",studioService.findAllStudio());
    return "gallery";
}

    @GetMapping("/shop")
    public String shop(Model model) throws IOException {
        List<ArtWork> artWorks = artworkService.getAllArtworks();
        model.addAttribute("artWorks", artWorks);
        model.addAttribute("headers",shopHeaderService.findAllShopHeader());
        model.addAttribute("exhibitions",exhibitionService.findAllExhibition());
        return "shop";
    }
    @GetMapping("/contact")
    public String contact(Model model) throws IOException {
        model.addAttribute("headers",contactHeaderService.findAllContactHeader());
        model.addAttribute("faqs",faqService.findAllFAQ());
        return "contact";
    }

    @GetMapping("/gallery/load")
    @ResponseBody
    public List<ArtWork> loadMoreArtworks(
            @RequestParam int page,
            @RequestParam(defaultValue = "9") int size) {

        return artworkService
                .getArtworksPage(page, size)
                .getContent();
    }

    @PostMapping("/contact/save")
    public String saveContact(@ModelAttribute Contact contact) {
        contactService.saveContact(contact);
        return "redirect:/contact";
    }


}
