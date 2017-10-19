package crwlr.api.rest.crawling.interfaces;

import java.util.Map;

/**
 * Created by haho on 19/10/2017.
 */
public interface ICrawlingService {
//  Map<String, String> getCustomerMessages(String lang);

  Map<String, Map<String, String>> getMessages(String lang);
}
