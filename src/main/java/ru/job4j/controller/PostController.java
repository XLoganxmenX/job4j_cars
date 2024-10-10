package ru.job4j.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dto.FileDto;
import ru.job4j.model.User;
import ru.job4j.service.CarModelService;
import ru.job4j.service.CarService;
import ru.job4j.service.EngineService;
import ru.job4j.service.PostService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {
    private final PostService postService;
    private final CarModelService carModelService;
    private final EngineService engineService;
    private final CarService carService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("carModels", carModelService.findAll());
        model.addAttribute("engines", engineService.findAll());
        model.addAttribute("postsDto", postService.findAllListPagePostDto());
        return "posts/list";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id, HttpSession session) {
        var postDto = postService.findPostDtoById(id);
        var user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("postDto", postDto);
        return "posts/one";
    }

    @GetMapping("/create")
    public String getCreatePage(Model model) {
        model.addAttribute("carModels", carModelService.findAll());
        model.addAttribute("engines", engineService.findAll());
        return "posts/create";
    }

    @PostMapping("/save")
    public String createPost(@RequestParam("engineId") int engineId,
                             @RequestParam("carModelId") int carModelId,
                             @RequestParam("carName") String carName,
                             @RequestParam("description") String description,
                             @RequestParam("price") int price,
                             @RequestParam List<MultipartFile> files,
                             Model model,
                             HttpSession session) {
        var user = (User) session.getAttribute("user");
        var created = LocalDateTime.now();
        List<FileDto> filesDto = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    filesDto.add(new FileDto(file.getOriginalFilename(), file.getBytes()));
                }
            }
            var car = carService.createCar(user, carName, engineId, carModelId);
            postService.createNewPost(user, description, created, price, car, filesDto, false);
            return "redirect:/posts";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/carModel-{id}")
    public String findByCarModel(Model model, @PathVariable int id) {
        var carModel = carModelService.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Неверный id модели авто"));
        model.addAttribute("carModels", carModelService.findAll());
        model.addAttribute("engines", engineService.findAll());
        model.addAttribute("postsDto", postService.findPostDtoByCarModel(carModel));
        return "posts/list";
    }

    @GetMapping("/engine-{id}")
    public String findByEngine(Model model, @PathVariable int id) {
        var engine = engineService.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Неверный id двигателя"));
        model.addAttribute("carModels", carModelService.findAll());
        model.addAttribute("engines", engineService.findAll());
        model.addAttribute("postsDto", postService.findPostDtoByEngine(engine));
        return "posts/list";
    }

    @PostMapping("/sell/{id}")
    public String sellPost(@PathVariable int id, Model model) {
        boolean isSold = postService.sellPostById(id);
        if (!isSold) {
            model.addAttribute("message", "Не удалось перевести объявление в статус \"Продан\"");
            return "errors/404";
        }
        return "redirect:/posts/" + id;
    }
}
