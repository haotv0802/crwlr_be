package crwlr.api.rest.person.picker.interfaces;

import crwlr.api.rest.person.picker.beans.PersonPresenter;

import java.util.List;

/**
 * Created by haho on 7/5/2017.
 */
public interface IPersonPickerService {
  List<PersonPresenter> getPersonsList(int userId);
}
