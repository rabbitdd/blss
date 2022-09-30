package main.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.bean.XmlFileUser;
import main.entity.User;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class XMLReader {

  // final private UserServiceImpl userService;
  static private final String FILE_SE = "/home/s284694/log.xml";
  static private final String FILE_NAME = "C:\\Users\\Admin\\OneDrive\\Рабочий стол\\Ucheb\\Labs\\BLPS\\BLPS3\\src\\main\\resources\\info.xml";

  public boolean writeUserInfo() throws JAXBException {
//    String fileNameHelios = "/home/s284694/log.xml";
//    String fileNameLocal = "C:\\Users\\Никита\\Desktop\\itmo\\6 семестр\\БЛПС\\BLPS1\\src\\main\\resources\\info.xml";
//    File file = new File(fileNameLocal);
//    List<User> users = userService.findAll();
//    XmlFileUser xmlFileUser = new XmlFileUser("users", users);
//    JAXBContext contextObj = JAXBContext.newInstance(XmlFileUser.class);
//    Marshaller marshallerObj = contextObj.createMarshaller();
//    marshallerObj.marshal(xmlFileUser, file);
//    log.info("write ....");
    return true;
  }

  public User getToken(String login) {
    try{
      File file = new File(FILE_NAME);
      log.info("file exist " + file.getName());
      String fileNameHelios =
          "C:\\Users\\Никита\\Desktop\\itmo\\6 семестр\\БЛПС\\BLPS1\\src\\main\\resources\\info.xml";
      //File file = new File(FILE_NAME);
      XmlFileUser fileUser = JAXB.unmarshal(file.getAbsoluteFile(), XmlFileUser.class);
      log.info("file user exitst " + fileUser.toString());
      StringBuilder stringBuilder = new StringBuilder();
      List<User> users = fileUser.getUsers();
      for (User user : users) {
        log.info(user.toString());
        if (user.getLogin().equals(login)) {
          log.info(user.getLogin());
          return user;
        }
      }
    } catch (Exception e) {
      log.info("exeeeeee");
      e.printStackTrace();
    }
//    File file = new File(FILE_SE);
//    //File file = new File(FILE_NAME);
//    XmlFileUser fileUser = JAXB.unmarshal(FILE_SE, XmlFileUser.class);
//    StringBuilder stringBuilder = new StringBuilder();
//    List<User> users = fileUser.getUsers();
//    for (User user : users) {
//      if (user.getLogin().equals(login))
//        return user;
//    }
    return new User();
  }

}
