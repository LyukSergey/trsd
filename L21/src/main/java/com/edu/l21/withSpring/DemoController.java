package com.edu.l21.withSpring;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoController {

    private final OrderService orderService;
    private final UserService userService;
    private final ConnectionPool connectionPool;
    private final EventPublisher eventPublisher;
    private final SingletonBean singletonBean;
    private final Solution1ObjectProvider solution1;
    private final Solution2Lookup solution2;
    private final Solution3ScopedProxyService solution3;
    private final Solution4ApplicationContext solution4;
    private final PrototypeInSingletonProblem prototypeInSingletonProblem;

    @GetMapping("/order")
    public String testOrder() {
        orderService.processOrder("ORD-123");
        return "Order processed - check console";
    }

    @GetMapping("/user")
    public String testUser() {
        return userService.getUser(1L);
    }

    @GetMapping("/connection")
    public String testConnection() {
        return connectionPool.getConnection();
    }

    @GetMapping("/event")
    public String testEvent() {
        eventPublisher.publishEvent("Test Event Message");
        return "Event published - check console";
    }

    @GetMapping("/singleton")
    public Map<String, Object> testSingleton() {
        Map<String, Object> result = new HashMap<>();
        result.put("hashCode", singletonBean.hashCode());
        result.put("counter", singletonBean.incrementAndGet());
        return result;
    }

    @GetMapping("/prototype/problem")
    public String testPrototypeProblem() {
        prototypeInSingletonProblem.doWork();
        return "Check PrototypeInSingletonProblem in console on startup";
    }

    @GetMapping("/prototype/solution1")
    public String testSolution1() {
        solution1.doWork();
        solution1.doWork();
        return "ObjectProvider solution - check console (different hashCodes)";
    }

    @GetMapping("/prototype/solution2")
    public String testSolution2() {
        solution2.doWork();
        solution2.doWork();
        return "@Lookup solution - check console (different hashCodes)";
    }

    @GetMapping("/prototype/solution3")
    public String testSolution3() {
        solution3.doWork();
        solution3.doWork();
        return "ScopedProxy solution - check console";
    }

    @GetMapping("/prototype/solution4")
    public String testSolution4() {
        solution4.doWork();
        solution4.doWork();
        return "ApplicationContext solution - check console (different hashCodes)";
    }
}


