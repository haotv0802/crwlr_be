package fm.api.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import crwlr.transaction.TransactionFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.restdocs.ManualRestDocumentation;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.constraints.ResourceBundleConstraintDescriptionResolver;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.session.ExpiringSession;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Map;

import static java.util.ResourceBundle.getBundle;
import static org.springframework.restdocs.cli.CliDocumentation.curlRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.util.StringUtils.collectionToDelimitedString;

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
public abstract class BaseDocumentation extends AbstractTransactionalTestNGSpringContextTests {
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

  public final ManualRestDocumentation restDocumentation = new ManualRestDocumentation("target/generated-snippets");

  @Autowired
  private SessionRepositoryFilter<? extends ExpiringSession> sessionRepositoryFilter;

  public static Locale locale = new Locale("en");

  @Autowired
  private TransactionFilter txFilter;

  @BeforeClass
  public void setup() throws UnknownHostException {
    final PrintWriter printWriter = IoBuilder.forLogger(logger).buildPrintWriter();

    mockMvc =
        MockMvcBuilders
            .webAppContextSetup(wac)
            .addFilter(sessionRepositoryFilter)
            .addFilter(txFilter)
            .apply(springSecurity())
            .apply(documentationConfiguration(this.restDocumentation)
                .uris()
                .withScheme("http")
                .withHost(InetAddress.getLocalHost().getHostName())
                .withPort(8080)
                .and()
                .snippets()
            )
            .alwaysDo(print(printWriter))
            .build();
  }

  @BeforeMethod
  public void setUp(Method method) {
    this.restDocumentation.beforeTest(getClass(), method.getName());
  }

  @AfterMethod
  public void tearDown() {
    this.restDocumentation.afterTest();
  }

}