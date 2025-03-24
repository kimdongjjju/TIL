package org.ttaengju.com.deepdive.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TargetService {

    void specificMethod(int specialData) {
      log.info("기존 서비스 기능 호출 {}",specialData);
    }

}
