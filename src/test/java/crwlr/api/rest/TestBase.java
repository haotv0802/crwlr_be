package crwlr.api.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;

import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.Locale;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Created by haho
 * Date:  05/05/2016 Time: 5:15 PM
 */
@WebAppConfiguration
@ContextConfiguration(
    locations = {
        "/config/spring-mvc.xml",
        "/config/spring-mvc-test.xml"
    })
public abstract class TestBase extends AbstractTestNGSpringContextTests {
  protected final Logger logger = LogManager.getLogger(getClass());

  protected MockMvc mockMvc;

  @Autowired
  @Qualifier("testObjectMapper")
  protected ObjectMapper objectMapper;

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  @Qualifier("tstMsgSource")
  private MessageSource messageSource;

  public static Locale locale = new Locale("en");

  @BeforeClass
  public void setup() throws UnknownHostException {
    final PrintWriter printWriter = IoBuilder.forLogger(logger).buildPrintWriter();

    mockMvc =
        MockMvcBuilders
            .webAppContextSetup(wac)
            .apply(springSecurity())
            .alwaysDo(print(printWriter))
            .build();
  }
}