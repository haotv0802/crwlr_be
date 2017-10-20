package crwlr.api.rest.crawling.beans;

/**
 * Date: 10/20/2017 Time: 10:56 AM
 * This Vendor Product is to be presented on Front-end.
 * @author haho
 */
public class VendorProductPresenter {
  private String name;
  private String category;
  private VendorPresenter vendor;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public VendorPresenter getVendor() {
    return vendor;
  }

  public void setVendor(VendorPresenter vendor) {
    this.vendor = vendor;
  }
}
