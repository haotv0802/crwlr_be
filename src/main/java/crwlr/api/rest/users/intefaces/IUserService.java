package crwlr.api.rest.users.intefaces;

import crwlr.api.rest.users.UserBean;

import java.util.List;

/**
 * Created by haho on 3/22/2017.
 */
public interface IUserService {
  List<UserBean> getUsers();
  void updateUsersRoles(List<UserBean> users);
}
