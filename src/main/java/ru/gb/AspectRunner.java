package ru.gb;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AspectRunner {

  private final List<Brother> brothers;

  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationReady() {
    System.out.println(brothers);
    for (Brother brother : brothers) {
    //  try {
        brother.method1("1", 2);
        brother.method2();
        System.out.println( brother.method3());

   //   } catch (Throwable e) {
    //    log.error("was exc");
      }
    }
  }


