package cn.aze.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import bean.Message;

/**
 * 
 * 列表页面初始化控制
 *
 */
@SuppressWarnings("serial")
public class ListServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {

			req.setCharacterEncoding("UTF-8");
			String command = req.getParameter("command");
			String description = req.getParameter("description");
			req.setAttribute("command", command);
			req.setAttribute("description", description);
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/weixn", "lili",
					"123456");
			StringBuilder sql = new StringBuilder("select ID,COMMAND,DESCRIPTION, CONTENT from MESSAGE where 1=1");
			List<String> paramList = new ArrayList<>();
		
			if (command != null && !"".equals(command.trim())) {
			    sql.append(" and COMMAND=?");
			    paramList.add(command);
			}
			if (description != null && !"".equals(description.trim())) {
			    sql.append(" and DESCRIPTION like '%' ? '%' ");
			    paramList.add(description);
			}

			PreparedStatement statement = conn.prepareStatement(sql.toString());
			for (int i = 0; i < paramList.size(); i++) {

				statement.setString(i+1, paramList.get(i));
			}

			ResultSet rSet = statement.executeQuery();
			List<Message> messageList = new ArrayList<Message>();

			while (rSet.next()) {
				Message message = new Message();
				messageList.add(message);
				message.setId(rSet.getString("ID"));
				message.setCommand(rSet.getString("COMMAND"));
				message.setDescription(rSet.getString("DESCRIPTION"));
				message.setContent(rSet.getString("CONTENT"));
			}
			req.setAttribute("messageList", messageList);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		req.getRequestDispatcher("/WEB-INF/jsp/back/list.jsp").forward(req, resp);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(req, resp);
	}
}
