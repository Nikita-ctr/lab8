package com.samusevich.lab8.servlet;

import com.samusevich.lab8.entity.ChatUser;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class LogoutServlet extends ChatServlet{

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String name = (String) request.getSession().getAttribute("name");


    // Если в сессии имеется имя пользователя...
        if (name!=null) {
    // Получить объект, описывающий пользователя с таким именем
            ChatUser aUser = activeUsers.get(name);

    // Если идентификатор сессии пользователя, вошедшего под этим именем, совпадает с идентификатором сессии
    // пользователя, пытающегося выйти из чата (т.е. выходит тот же, кто и входил)
            if (aUser.getSessionId().equals((String)
                    request.getSession().getId())) {
    // Удалить пользователя из списка активных Т.к. запросы обрабатываются одновременно, нужна синхронизация
                synchronized (activeUsers) {
                    activeUsers.remove(name);
                }


    // Сбросить имя пользователя в сессии
                request.getSession().setAttribute("name", null);


    // Сбросить ID сессии в cookie
                response.addCookie(new Cookie("sessionId", null));


    // Перенаправить на главную страницу
                response.sendRedirect(response.encodeRedirectURL("/chat/"));


            } else {
    // Пользователь пытается аннулировать чужую сессию – не делать ничего
                response.sendRedirect(response.encodeRedirectURL("/chat/view.htm"));

            }
        } else {
    // Перенаправить пользователя на главное окно чата
            response.sendRedirect(response.encodeRedirectURL("/chat/view.htm"));

        }
    }
}


