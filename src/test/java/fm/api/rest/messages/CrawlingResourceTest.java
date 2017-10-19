package fm.api.rest.messages;

import fm.api.rest.TestBase;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by haho on 19/10/2017.
 */
public class CrawlingResourceTest extends TestBase {

  @Test
  public void testGetDataCrawled() throws Exception {
    mockMvc
        .perform(get("/svc/crawler/getDataCrawled")
        )
        .andExpect(status().is(200))
    ;
  }
}