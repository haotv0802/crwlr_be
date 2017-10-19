package crwlr.api.rest.crawling.interfaces;

import crwlr.api.rest.crawling.beans.VendorProduct;

import java.util.Map;
import java.util.Set;

/**
 * Created by haho on 6/12/2017.
 */
public interface ICrawlingDao {
  void saveCrawledData(Map<String, Set<VendorProduct>> data);
}
