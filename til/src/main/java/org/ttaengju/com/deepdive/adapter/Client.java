package org.ttaengju.com.deepdive.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Client {

    private final Target adapter;
    @GetMapping("/adapter")
    public void client() {

        adapter.method(1);
    }
}
