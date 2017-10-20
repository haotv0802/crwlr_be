package crwlr.api.rest.crawling.interfaces;

import crwlr.api.rest.crawling.beans.Vendor;
import crwlr.api.rest.crawling.beans.VendorProduct;

/**
 * Date: 10/19/2017 Time: 4:56 PM
 *
 * @author haho
 */
public interface ICrawlingDao {
  void saveVendor(Vendor vendor);
  void saveVendorProduct(VendorProduct product, String vendorName);
}
