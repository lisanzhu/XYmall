package com.xymall.baby.service.impl;
import com.xymall.baby.Utils.BeanUtil;
import com.xymall.baby.Utils.MyUtils;
import com.xymall.baby.Utils.PageQueryUtil;
import com.xymall.baby.Utils.PageResult;
import com.xymall.baby.common.Constant;
import com.xymall.baby.common.ServiceResultEnum;
import com.xymall.baby.dao.UserMapper;
import com.xymall.baby.entity.User;
import com.xymall.baby.service.UserService;
import com.xymall.baby.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;
    @Override
    public String login(String userName, String password, HttpSession httpSession) {
        User user = userMapper.login(userName, password);

        if (user != null && httpSession != null) {
            if (!user.isActive()) {
                return ServiceResultEnum.LOGIN_USER_LOCKED.getResult();
            }
            //昵称太长 影响页面展示
            if (user.getUserName() != null && user.getUserName().length() > 7) {
                String tempNickName = user.getUserName().substring(0, 7) + "..";
                user.setUserName(tempNickName);
            }
            UserVO userVO = new UserVO();
            BeanUtil.copyProperties(user, userVO);
            //设置购物车中的数量
            httpSession.setAttribute(Constant.MALL_USER_SESSION_KEY, userVO);
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }

//    @Override
//    public void register(String userName, String password, String email) {
//        userMapper.register(userName, email, password);
//    }

    @Override
    public PageResult getUserPage(PageQueryUtil pageQueryUtil) {
        List<User> list= userMapper.findUserList(pageQueryUtil);
        int total=userMapper.countUserNum();
        return new PageResult(list,total,pageQueryUtil.getLimit(), pageQueryUtil.getPage());
    }

    @Override
    public boolean lockUser(Integer[] ids, int lockStatus) {
        if (ids.length < 1) {
            return false;
        }
        return userMapper.lockUser(ids,lockStatus)>0;
    }

    @Override
    public UserVO updateUserInfo(User user, HttpSession httpSession) {
        UserVO userTemp = (UserVO) httpSession.getAttribute(Constant.MALL_USER_SESSION_KEY);
        User userFromDB = userMapper.selectByPrimaryKey(userTemp.getUserId());
//        System.out.println(userFromDB.getUserId());
//        System.out.println(userFromDB.getUserName());
//        System.out.println(userFromDB.getUserName());
        if (userFromDB != null) {
//            System.out.println(user.getUserName());
//            System.out.println(user.getAddress());
            userFromDB.setAddress(MyUtils.cleanString(user.getAddress()));
//            System.out.println(userFromDB.getUserName());
//            userFromDB.setIntroduceSign(NewBeeMallUtils.cleanString(mallUser.getIntroduceSign()));
            if (userMapper.updateByPrimaryKeySelective(userFromDB) > 0) {
                UserVO userVO = new UserVO();
                userFromDB = userMapper.selectByPrimaryKey(user.getUserId());
//                System.out.println(user.getUserId());
//                System.out.println(userFromDB.getAddress());
                BeanUtil.copyProperties(userFromDB, userVO);
                httpSession.setAttribute(Constant.MALL_USER_SESSION_KEY, userVO);

                return userVO;
            }
        }
        return null;
    }

    @Override
    public String register(String loginName, String password) {
        if (userMapper.selectByUserName(loginName) != null) {
            return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
        }
        User registerUser = new User();
        registerUser.setUserName(loginName);
        registerUser.setPassword(password);
        registerUser.setActive(true);
        registerUser.setAddress("");
        if (userMapper.insertSelective(registerUser) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

}
