package ch.judos.snakes.region.controller;

import ch.judos.snakes.region.service.BaseJavaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController()
@RequestMapping("java")
public class JavaController {

    private final BaseJavaService javaService;
    @Autowired
    public JavaController(BaseJavaService javaService) {
        this.javaService = javaService;
    }

//    @PostMapping(path = "test")
//    public String test(@Valid @RequestBody OrderDto order) {
//        return "Java Test works!";
//    }

}

class OrderDto {
    @NotNull long rewardId; // Notnull validation doesn't work
}

class OrderDto1 {
    @NotNull Long rewardId; // NotNull works, but type is nullable which we don't want
}

class OrderDto2 {
    @Min(1) long rewardId; // NotNull works, different validation than what we normally want
}
