package com.jtarget.web.servlet;

import com.jtarget.dao.UserDao;
import com.jtarget.domain.User;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.设置编码
        req.setCharacterEncoding("utf-8");

        /**
         * //2.获取请求参数
         *         String username = req.getParameter("username");
         *         String password = req.getParameter("password");
         *         //3.封装user对象
         *         User loginUser = new User();
         *         loginUser.setUsername(username);
         *         loginUser.setPassword(password);
         */

        //2.获取所有请求参数
        Map<String, String[]> map = req.getParameterMap();

        //3.创建USer对象
        User loginUser = new User();

        //3.2使用BeanUtils封装
        try {
            BeanUtils.populate(loginUser, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //4.把获取到的loginUser传入UserDao的login方法
        UserDao dao = new UserDao();
        User user = dao.login(loginUser);

        //5.判断UserDao传回的值，如果是User则成功，如果是null则失败
        if (user == null){
            //转发
            req.getRequestDispatcher("/failServlet").forward(req,resp);
        }else {
            //登陆成功，存储数据
            req.setAttribute("user",user);
            //转发
            req.getRequestDispatcher("/successServlet").forward(req,resp);
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
