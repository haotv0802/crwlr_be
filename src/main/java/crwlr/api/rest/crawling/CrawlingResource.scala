package crwlr.api.rest.crawling

import java.util.List

import crwlr.api.rest.crawling.beans.VendorProductPresenter
import crwlr.api.rest.crawling.interfaces.{ICrawledDataService, ICrawlingService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, RestController}

@RestController("crawlingResource")
@ComponentScan
@RequestMapping(path = Array("/svc"))
class CrawlingResource(
                   @Autowired val crawlingService: ICrawlingService,
                   @Autowired val crawledService: ICrawledDataService
                 ) {

  @GetMapping(path = Array("/crawler/crawledData"))
  def getCrawledData(): List[VendorProductPresenter] = {
    crawledService.getAllVendorProducts
  }

}

object CrawlingResource {
  def main(args: Array[String]) {
    println("Hello, world!")
  }
}

//@RestController
//@RequestMapping(path = ("/api"))
//class UserController(@Autowired val userService:UserService,@Autowired val dataSource:DataSource){
//@GetMapping(path = ("/users"))
//    def getAllUsers():Iterable[Users]={
//            userService.listUsers
//            }
//
