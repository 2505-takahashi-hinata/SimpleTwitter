package chapter6.controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chapter6.beans.User;
import chapter6.beans.UserMessage;
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/index.jsp" })
public class TopServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

    /**  //ログが出力できるようにする
    * ロガーインスタンスの生成
    */
    Logger log = Logger.getLogger("twitter");

    /**
    * デフォルトコンストラクタ
    * アプリケーションの初期化を実施する。
    */
    public TopServlet() {
        InitApplication application = InitApplication.getInstance();
        application.init();

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

      //実際にログを出力
	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());
	  //10追記
      boolean isShowMessageForm = false;
      User user = (User) request.getSession().getAttribute("loginUser");
      if (user != null) {
          isShowMessageForm = true;
      }

      //11追記
      List<UserMessage> messages = new MessageService().select();

      request.setAttribute("messages", messages);
      //11ここまで
      request.setAttribute("isShowMessageForm", isShowMessageForm);
      //10ここまで
      request.getRequestDispatcher("/top.jsp").forward(request, response);
    }
}

