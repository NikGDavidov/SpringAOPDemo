package ru.gb;

import org.slf4j.event.Level;
import org.springframework.stereotype.Component;
import ru.gb.aspect.Loggable;
import ru.gb.aspect.RecoverException;
import ru.gb.aspect.Timer;

@Component
public class Louiggi implements Brother {
  @Timer(level = Level.WARN)
 @Loggable(level = Level.WARN)
  public void method1(String arg1, int arg2) {

  }

  @Loggable(level = Level.WARN)
  @Timer(level = Level.WARN)
  public String method2() {
    return "value";
  }
@RecoverException
  public String method3() {

      throw new ArithmeticException("runtimeexceptionmsg");
  }

}
