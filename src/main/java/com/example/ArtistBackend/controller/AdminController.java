package com.example.ArtistBackend.controller;

import com.example.ArtistBackend.dto.UpdateOrderStatusDTO;
import com.example.ArtistBackend.model.*;
import com.example.ArtistBackend.repository.OrderRepository;
import com.example.ArtistBackend.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ArtworkService artworkService;

    @Autowired
    private  HomeHeroService homeHeroService;

    @Autowired
    private TestimonialService testimonialService;

    @Autowired
    private AboutHeaderService aboutHeaderService;

    @Autowired
    private GalleryHeaderService galleryHeaderService;

    @Autowired
    private ShopHeaderService shopHeaderService;

    @Autowired
    private ContactHeaderService contactHeaderService;

    @Autowired
    private AwardService awardService;

    @Autowired
    private ExhibitionService exhibitionService;

    @Autowired
    private FAQService faqService;

    @Autowired
    private StudioService studioService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;


    // ---------------------------------------------------------------
    // Adds messageCount to Model on EVERY admin page automatically
    // ---------------------------------------------------------------
    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        model.addAttribute("messageCount", contactService.getMessageCount());
    }


    @GetMapping("")
    public String dashboard(Model model) {
        model.addAttribute("activeTab", "dashboard");
        model.addAttribute("pageTitle", "Dashboard Overview");
        model.addAttribute("sectionContent", "admin-sections/dashboard");
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        System.out.println("Logged user: " + auth.getName());
        System.out.println("Roles: " + auth.getAuthorities());
        return "admin";
    }
    @GetMapping("/pages")
    public String pages(Model model) throws IOException {
        model.addAttribute("heroList",homeHeroService.getAllHeroes());
        model.addAttribute("testimonials",testimonialService.findAllTestimonial());
        model.addAttribute("pageHeaders",aboutHeaderService.findAllHeader());
        model.addAttribute("galleryHeaders",galleryHeaderService.findAllGalleryHeader());
        model.addAttribute("shopHeaders",shopHeaderService.findAllShopHeader());
        model.addAttribute("contactHeaders",contactHeaderService.findAllContactHeader());
        model.addAttribute("exhibitions",exhibitionService.findAllExhibition());
        model.addAttribute("awards",awardService.findAllAwards());
        model.addAttribute("FAQs",faqService.findAllFAQ());
        model.addAttribute("studios",studioService.findAllStudio());
        model.addAttribute("homeHero", new HomeHero());
        model.addAttribute("activeTab", "pages");
        model.addAttribute("pageTitle", "Manage Site Content");
        model.addAttribute("sectionContent", "admin-sections/pages");
        return "admin";
    }
    @GetMapping("/artworks")
    public String artworks(Model model) throws IOException {
//        System.out.println(artworkService.getAllArtworks());
        model.addAttribute("artworks", artworkService.getAllArtworks() );
        model.addAttribute("activeTab", "artworks");
        model.addAttribute("pageTitle", "Manage Artworks");
        System.out.println("🔥 SECTION: " + "admin-sections/artworks");
        model.addAttribute("sectionContent", "admin-sections/artworks");
        return "admin";
    }
    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("activeTab", "orders");
        model.addAttribute("pageTitle", "Order Management");
        model.addAttribute("sectionContent", "admin-sections/orders");

        List<Order> orders = orderRepository.findAllByOrderByIdDesc();
        model.addAttribute("orders", orders);
        return "admin";
    }

    @PostMapping("/orders/update-status")
    public String updateOrderStatus(
            @ModelAttribute UpdateOrderStatusDTO dto) {
        System.out.println("orderNumber" + dto.getOrderNumber());
        orderService.updateOrderStatus(
                dto.getOrderNumber(),
                dto.getStatus()
        );

        return "redirect:/admin/orders";
    }

    @GetMapping("/messages")
    public String messages(Model model) {
        model.addAttribute("messages", contactService.findAllContacts());
        model.addAttribute("messageCount", contactService.getMessageCount());
        model.addAttribute("activeTab", "messages");
        model.addAttribute("pageTitle", "Messages");
        model.addAttribute("sectionContent", "admin-sections/messages");
        return "admin";
    }


    @GetMapping("/artworks/edit/{id}")
    @ResponseBody
    public ArtWork getArtworkForEdit(@PathVariable Long id) {

        return artworkService.getArtworkById(id);
    }


//    <------------------------Artwork Post Mapings-------------------->
    @PostMapping("/artworks/add")
    public String addArtwork(@ModelAttribute ArtWork artwork,
                             @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        try {
            artworkService.saveArtwork(artwork, imageFile);
        } catch (IOException e) {
            System.out.println("Upload failed: " + e.getMessage());
            // Don't crash - continue to redirect
        }
    return "redirect:/admin/artworks";
    }
    @PostMapping("/artworks/delete/{id}")
    public String deleteArtwork(@PathVariable Long id) {
        artworkService.delete(id);
        return "redirect:/admin/artworks";
    }


    @PostMapping("/artworks/update")
    public String updateArtwork(@ModelAttribute ArtWork artwork,
                                @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws IOException {
        try {
            artworkService.updateArtwork(artwork, imageFile);
        } catch (IOException e) {
            System.out.println("Upload failed: " + e.getMessage());
        }
        return "redirect:/admin/artworks";
    }



    //    <------------------------Section Content Home Page Post Mapings-------------------->


    @PostMapping(value="/pages/home/hero/add")
    public String addHero (
            @ModelAttribute HomeHero hero,
            @RequestParam(value="imageFile", required=false) MultipartFile imageFile,
            jakarta.servlet.http.HttpServletRequest request) throws IOException {

        if (hero.getHeading() == null || hero.getHeading().trim().isEmpty()) {
            throw new IllegalArgumentException("Heading is required.");
        }
        if (hero.getSubtext() == null || hero.getSubtext().trim().isEmpty()) {
            throw new IllegalArgumentException("Subtext is required.");
        }
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Slide image is required.");
        }

        System.out.println("heading: " + hero.getHeading());
        System.out.println("subtext: " + hero.getSubtext());
        System.out.println("image: " + imageFile.getOriginalFilename());
        
        homeHeroService.SaveChanges(hero, imageFile);
        return "redirect:/admin/pages";
    }

    @PostMapping("/pages/home/hero/delete/{id}")
    public String deleteHero (@PathVariable Long id) throws IOException {
        homeHeroService.deleteHero(id);
        return "redirect:/admin/pages";
    }
    @GetMapping("/pages/home/hero/edit/{id}")
    @ResponseBody
    public HomeHero editHero(@PathVariable Long id) throws IOException {
        return homeHeroService.getById(id);
    }

    @PostMapping("/pages/home/hero/update")
    public String update(
            @ModelAttribute HomeHero homeHero,
            @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws IOException {
            
        if (homeHero.getHeading() == null || homeHero.getHeading().trim().isEmpty()) {
            throw new IllegalArgumentException("Heading is required to update a Hero Slide.");
        }
        if (homeHero.getSubtext() == null || homeHero.getSubtext().trim().isEmpty()) {
            throw new IllegalArgumentException("Subtext is required to update a Hero Slide.");
        }

        System.out.println("new update from controller item:" + homeHero.getHeading());
        System.out.println("new update from controller text:" + homeHero.getSubtext());

        homeHeroService.updateHero(homeHero, imageFile);
        return  "redirect:/admin/pages";
    }


//    <-------------------- Testimonial section Post Maping  ------------------------>

    @PostMapping("/pages/home/testimonial/add")
    public String addTestimonial (@ModelAttribute Testimonial testimonial,
                                  @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws IOException {
        testimonialService.saveTestimonial(testimonial, imageFile);
        return "redirect:/admin/pages";
    }

    @PostMapping("/pages/home/testimonial/delete/{id}")
    public String DeleteTestimonial(@PathVariable Long id) throws IOException {
        testimonialService.DeleteTestimonial(id);
        return  "redirect:/admin/pages";
    }

    @PostMapping("/pages/home/testimonial/update")
    public String UpdateTestimonial(@ModelAttribute Testimonial testimonial,
                                    @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws IOException {
        testimonialService.updateTestimonial(testimonial, imageFile);
        return "redirect:/admin/pages";
    }

    @GetMapping("/pages/home/testimonial/edit/{id}")
    @ResponseBody
    public Testimonial editTestimonial(@PathVariable Long id)  {
        return testimonialService.findTestimonialById(id);
    }


//    <----------------------------------- About Page Post Maping  ----------------------------->
    @PostMapping(value="/pages/about/header/add")
    public String addAboutHeader (@ModelAttribute AboutHero aboutHero,
                              @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        System.out.println("about header image"+ imageFile );
        aboutHeaderService.saveHeader(aboutHero, imageFile);
        return "redirect:/admin/pages";
    }

    @PostMapping("/pages/about/header/delete/{id}")
    public String DeleteAboutHeader(@PathVariable Long id) throws IOException {
        aboutHeaderService.DeleteHeader(id);
        return  "redirect:/admin/pages";
    }

    @PostMapping("/pages/about/header/update")
    public String updateAboutHeader(@ModelAttribute AboutHero aboutHero,
                                    @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws IOException {
        System.out.println("in about header console");
        aboutHeaderService.updateHeader(aboutHero, imageFile);
        return "redirect:/admin/pages";
    }

    @GetMapping("/pages/about/header/edit/{id}")
    @ResponseBody
    public AboutHero editAboutHeader(@PathVariable Long id)  {
        return aboutHeaderService.findHeaderById(id);
    }


//    <---------------------- Awards  ------------------->

    @PostMapping("/pages/about/awards/add")
    public String addAwards(@ModelAttribute Awards awards,
                            @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws IOException {
         awardService.saveAwards(awards,imageFile);
         return  "redirect:/admin/pages";
    }
    @PostMapping("/pages/about/awards/delete/{id}")
    public String DeleteAwards(@PathVariable Long id) throws IOException {
        awardService.deleteAwardsById(id);
        return "redirect:/admin/pages";
    }
    @PostMapping("/pages/about/awards/update")
    public String UpdateAwards(@ModelAttribute Awards awards,
                               @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws IOException {
        awardService.updateAwards(awards, imageFile);
        return  "redirect:/admin/pages";
    }

    @GetMapping("/pages/about/awards/edit/{id}")
    @ResponseBody
    public Awards editAwards(@PathVariable Long id) throws Exception {
        return awardService.getAwardById(id);
    }

    //    <--------------------------------- gallery Post maping ---------------------------------->
@PostMapping("/pages/gallery/header/add")
public String addGalleryHeader (@ModelAttribute GalleryHero galleryHero,
                                @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws IOException {
    galleryHeaderService.saveGalleryHeader(galleryHero, imageFile);
    return "redirect:/admin/pages";
}

    @PostMapping("/pages/gallery/header/delete/{id}")
    public String DeleteGalleryHeader(@PathVariable Long id) throws IOException {
        galleryHeaderService.DeleteGalleryHeader(id);
        return  "redirect:/admin/pages";
    }

    @PostMapping("/pages/gallery/header/update")
    public String updateGalleryHeader(@ModelAttribute GalleryHero galleryHero,
                                      @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws IOException {
        galleryHeaderService.updateGalleryHeader(galleryHero, imageFile);
        return "redirect:/admin/pages";
    }

    @GetMapping("/pages/gallery/header/edit/{id}")
    @ResponseBody
    public GalleryHero editGalleryHeader(@PathVariable Long id)  {
        return galleryHeaderService.findGalleryHeaderById(id);
    }

//    <-------------- studio ---------------->
@PostMapping("/pages/gallery/studio/add")
public String addStudio(@ModelAttribute Studio studio) throws IOException {
   studioService.saveStudio(studio);
    return "redirect:/admin/pages";
}

    @PostMapping("/pages/gallery/studio/delete/{id}")
    public String DeleteStudio(@PathVariable Long id) throws IOException {
        studioService.deleteStudioById(id);
        return  "redirect:/admin/pages";
    }

    @PostMapping("/pages/gallery/studio/update")
    public String updateStudio(@ModelAttribute Studio studio) throws IOException {
        studioService.updateStudio(studio);
        return "redirect:/admin/pages";
    }

    @GetMapping("/pages/gallery/studio/edit/{id}")
    @ResponseBody
    public Studio editStudio(@PathVariable Long id) throws Exception {
        return studioService.getStudioById(id);
    }

//    <---------------------------------- Shop Post Maping --------------------------------->

    @PostMapping("/pages/shop/header/add")
    public String addShopHeader (@ModelAttribute ShopHero shopHero,
                                 @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws IOException {
        shopHeaderService.saveShopHeader(shopHero, imageFile);
        return "redirect:/admin/pages";
    }

    @PostMapping("/pages/shop/header/delete/{id}")
    public String DeleteShopHeader(@PathVariable Long id) throws IOException {
        shopHeaderService.DeleteShopHeader(id);
        return  "redirect:/admin/pages";
    }

    @PostMapping("/pages/shop/header/update")
    public String updateShopHeader(@ModelAttribute ShopHero shopHero,
                                   @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws IOException {
        shopHeaderService.updateShopHeader(shopHero, imageFile);
        return "redirect:/admin/pages";
    }

    @GetMapping("/pages/shop/header/edit/{id}")
    @ResponseBody
    public ShopHero editShopHeader(@PathVariable Long id)  {
        return shopHeaderService.findShopHeaderById(id);
    }

//    <--------------- Exhibitionn ---------------->
@PostMapping("/pages/shop/exhibition/add")
public String addExhibition (@ModelAttribute Exhibition exhibition,
                             @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws IOException {
    exhibitionService.saveExhibition(exhibition, imageFile);
    return "redirect:/admin/pages";
}

    @PostMapping("/pages/shop/exhibition/delete/{id}")
    public String DeleteExhibition (@PathVariable Long id) throws IOException {
        exhibitionService.deleteExhibitionById(id);
        return  "redirect:/admin/pages";
    }

    @PostMapping("/pages/shop/exhibition/update")
    public String updateExhibition(@ModelAttribute Exhibition exhibition,
                                   @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws IOException {
        exhibitionService.updateExhibition(exhibition, imageFile);
        return "redirect:/admin/pages";
    }

    @GetMapping("/pages/shop/exhibition/edit/{id}")
    @ResponseBody
    public Exhibition editExhibition(@PathVariable Long id) throws Exception {
        return exhibitionService.getExhibitionById(id);
    }

//    <--------------------------------------- Contact Post Maping ----------------------------------->

    @PostMapping("/pages/contact/header/add")
    public String addContactHeader (@ModelAttribute ContactHero contactHero,
                                    @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws IOException {
        contactHeaderService.saveContactHeader(contactHero, imageFile);
        return "redirect:/admin/pages";
    }

    @PostMapping("/pages/contact/header/delete/{id}")
    public String DeleteContactHeader(@PathVariable Long id) throws IOException {
        contactHeaderService.DeleteContactHeader(id);
        return  "redirect:/admin/pages";
    }

    @PostMapping("/pages/contact/header/update")
    public String updateContactHeader(@ModelAttribute ContactHero contactHero,
                                      @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws IOException {
        contactHeaderService.updateContactHeader(contactHero, imageFile);
        return "redirect:/admin/pages";
    }

    @GetMapping("/pages/contact/header/edit/{id}")
    @ResponseBody
    public ContactHero editContactHeader(@PathVariable Long id)  {
        return contactHeaderService.findContactHeaderById(id);
    }

//    <-------------------- FAQ -------------------->

    @PostMapping("/pages/contact/FAQ/add")
    public String addFAQ (@ModelAttribute FAQ faq) throws IOException {
        faqService.saveFAQ(faq);
        return "redirect:/admin/pages";
    }

    @PostMapping("/pages/contact/FAQ/delete/{id}")
    public String DeleteFAQ(@PathVariable Long id) throws IOException {
        faqService.deleteFAQById(id);
        return  "redirect:/admin/pages";
    }

    @PostMapping("/pages/contact/FAQ/update")
    public String updateFAQ(@ModelAttribute FAQ faq) throws IOException {
        faqService.updateFAQ(faq);
        return "redirect:/admin/pages";
    }

    @GetMapping("/pages/contact/FAQ/edit/{id}")
    @ResponseBody
    public FAQ editFAQ(@PathVariable Long id) throws Exception {
        return faqService.getFAQById(id);
    }

}
