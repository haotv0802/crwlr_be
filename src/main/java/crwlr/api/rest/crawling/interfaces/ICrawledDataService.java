package crwlr.api.rest.crawling.interfaces;

import crwlr.api.rest.crawling.beans.VendorProductPresenter;
import crwlr.api.rest.crawling.beans.VendorProductPresenter2;

import java.util.List;

/**
 * Date: 10/20/2017 Time: 10:18 AM
 * TODO: WRITE THE DESCRIPTION HERE
 *
 * @author haho
 */
public interface ICrawledDataService {
  List<VendorProductPresenter2> getAllVendorProducts();
}
