//package testing;
//
//import crwlr.api.rest.crawling.beans.Vendor;
//import crwlr.api.rest.crawling.beans.VendorPresenter;
//import crwlr.api.rest.crawling.beans.VendorProductPresenter;
//import crwlr.api.rest.crawling.interfaces.ICrawledDataService;
//import crwlr.api.rest.crawling.interfaces.ICrawlingService;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.util.Assert;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//@RestController
//@RequestMapping(path = ("/api"))
//class UserController(@Autowired val userService:UserService,@Autowired val dataSource:DataSource){
//@GetMapping(path = ("/users"))
//    def getAllUsers():Iterable[Users]={
//            userService.listUsers
//            }
//
//@GetMapping(path = "/users/{id}")
//    def getUser(@PathVariable id:Long):Users={
//        userService.getUser(id)
//        }
//
//@PostMapping(path = ("/users"))
//    def createUser(@RequestBody users:Users):ResponseEntity[Long]={
//        val id=userService.createUser(users)
//        new ResponseEntity(id,new HttpHeaders,HttpStatus.CREATED)
//        }
//        }