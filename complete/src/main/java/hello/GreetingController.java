package hello;

import java.util.concurrent.atomic.AtomicLong;

import com.pi4j.platform.PlatformAlreadyAssignedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        try {
            GPIOSingleton.getInstance().onAWhile();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
}
