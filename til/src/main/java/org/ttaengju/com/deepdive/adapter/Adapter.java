package org.ttaengju.com.deepdive.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Adapter implements Target {

    private final TargetService targetService;

    @Override
    public void method(int data) {
        targetService.specificMethod(data);
    }
}
