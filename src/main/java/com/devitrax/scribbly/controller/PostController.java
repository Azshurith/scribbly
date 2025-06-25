package com.devitrax.scribbly.controller;

import com.devitrax.scribbly.model.Post;
import com.devitrax.scribbly.model.User;
import com.devitrax.scribbly.repository.UserRepository;
import com.devitrax.scribbly.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.security.Principal;

/**
 * Controller responsible for handling blog post-related operations such as viewing, creating,
 * editing, and deleting posts.
 */
@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final UserRepository userRepo;

    public PostController(PostService postService, UserRepository userRepo) {
        this.postService = postService;
        this.userRepo = userRepo;
    }

    /**
     * Displays a paginated list of blog posts.
     *
     * @param page  current page number (default 0)
     * @param size  number of posts per page (default 5)
     * @param model Spring UI model
     * @return the list view template
     */
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> postPage = postService.getByPage(pageable);

        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());
        return "post/list";
    }

    /**
     * Displays the post form for creating or editing a post.
     *
     * @param id        "new" or post ID
     * @param model     Spring UI model
     * @param principal authenticated user
     * @return the form view template
     */
    @GetMapping("/form/{id}")
    public String form(@PathVariable String id, Model model, Principal principal) {
        if ("new".equalsIgnoreCase(id)) {
            model.addAttribute("post", new Post());
            return "post/form";
        }

        try {
            Long postId = Long.parseLong(id);
            Post post = postService.getPostById(postId);
            if (!post.getUser().getUsername().equals(principal.getName())) {
                return "redirect:/post/list";
            }
            model.addAttribute("post", post);
            return "post/form";
        } catch (NumberFormatException e) {
            return "redirect:/post/list";
        }
    }

    /**
     * Displays a single blog post.
     *
     * @param id    post ID
     * @param model Spring UI model
     * @return the post view template
     */
    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);
        return "post/view";
    }

    /**
     * Handles post creation.
     *
     * @param post      new post data
     * @param principal authenticated user
     * @return redirect to post list
     */
    @PostMapping("/create")
    public String create(@ModelAttribute Post post, Principal principal) {
        User user = userRepo.findByUsername(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));

        post.setUser(user);
        postService.save(post);
        return "redirect:/post/list";
    }

    /**
     * Handles post updates.
     *
     * @param id          post ID
     * @param updatedPost updated post data
     * @param principal   authenticated user
     * @return redirect to the updated post view
     */
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Post updatedPost, Principal principal) {
        Post post = postService.getPostById(id);

        if (!post.getUser().getUsername().equals(principal.getName())) {
            return "redirect:/post/list";
        }

        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        postService.save(post);
        return "redirect:/post/" + post.getId();
    }

    /**
     * Handles post deletion.
     *
     * @param id        post ID
     * @param principal authenticated user
     * @return redirect to post list
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Principal principal) {
        Post post = postService.getPostById(id);

        if (!post.getUser().getUsername().equals(principal.getName())) {
            return "redirect:/post/list";
        }

        postService.deleteById(id);
        return "redirect:/post/list";
    }
}
