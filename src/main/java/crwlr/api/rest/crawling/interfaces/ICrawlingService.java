package crwlr.api.rest.crawling.interfaces;

import crwlr.api.rest.crawling.beans.VendorProduct;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by haho on 19/10/2017.
 */
public interface ICrawlingService {
  Map<String, Set<VendorProduct>> saveCrawledData(List<String> pages);
}
