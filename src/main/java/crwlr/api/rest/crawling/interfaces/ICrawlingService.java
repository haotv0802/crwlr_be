package crwlr.api.rest.crawling.interfaces;

import crwlr.api.rest.crawling.beans.Vendor;

import java.util.List;
import java.util.Map;

/**
 * Created by haho on 19/10/2017.
 */
public interface ICrawlingService {
  Map<String, Vendor> saveCrawledData(List<String> pages);
}
