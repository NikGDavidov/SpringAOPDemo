package ru.gb;

import org.slf4j.event.Level;
import org.springframework.stereotype.Component;
import ru.gb.aspect.Loggable;
import ru.gb.aspect.RecoverException;
import ru.gb.aspect.Timer;

@Loggable(level = Level.INFO)
@Timer(level = Level.INFO)
@Component
public class Mario implements Brother {

  public void method1(String arg1, int arg2) {

  }

  public String method2() {
    return "value";
  }
@RecoverException
  public String method3() {
    throw new ArithmeticException("runtimeexceptionmsg");
    //return"mario 3";
  }

}
