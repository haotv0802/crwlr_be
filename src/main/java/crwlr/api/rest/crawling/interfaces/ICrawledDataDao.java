package crwlr.api.rest.crawling.interfaces;

import crwlr.api.rest.crawling.beans.VendorPresenter;
import crwlr.api.rest.crawling.beans.VendorProductPresenter;
import crwlr.api.rest.crawling.beans.VendorProductPresenter2;

import java.util.List;

/**
 * Date: 10/20/2017 Time: 9:44 AM
 *
 * @author haho
 */
public interface ICrawledDataDao {
  List<VendorProductPresenter> getVendorProductsByVendorName(VendorPresenter vendor);
  List<VendorPresenter> getAllVendors();
  List<VendorProductPresenter2> getAllVendorProducts();
}
