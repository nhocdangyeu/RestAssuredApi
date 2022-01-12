package gss.workshop.testing.utils;

import java.util.logging.Logger;

public class OtherUtils {

  private static final Logger logger = Logger.getLogger(String.valueOf(OtherUtils.class));

  public static String randomBoardName() {
    String name = "My board " + (int) (Math.random() * 10000);
    logger.info(String.format("Random board text: %s", name));
    return name;
  }

  public static String randomTaskName() {
    String name = "My task " + (int) (Math.random() * 10000);
    logger.info(String.format("Random Task text: %s", name));
    return name;
  }
}
