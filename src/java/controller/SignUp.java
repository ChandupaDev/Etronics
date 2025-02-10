package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.User;
import dto.Response_DTO;
import dto.User_DTO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import model.Mail;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "SignUp", urlPatterns = {"/SignUp"})
public class SignUp extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Response_DTO response_dto = new Response_DTO();

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        User_DTO user_dto = gson.fromJson(request.getReader(), User_DTO.class);

        if (user_dto.getFirst_name().isEmpty()) {
            response_dto.setContent("Please Enter Your First Name");
        } else if (user_dto.getLast_name().isEmpty()) {
            response_dto.setContent("Please Enter Your Last Name");
        } else if (user_dto.getEmail().isEmpty()) {
            response_dto.setContent("Please Enter Your Email");
        } else if (!Validation.isEmailValid(user_dto.getEmail())) {
            response_dto.setContent("Please Enter A Valid Email");
        } else if (user_dto.getPassword().isEmpty()) {
            response_dto.setContent("Please Enter Your Password");
        } else if (!Validation.isPasswordValid(user_dto.getPassword())) {
            response_dto.setContent("Password must be include at least one number, upperclass letter, special charator and at least 8 charators long");
        } else {

            Session session = HibernateUtil.getSessionFactory().openSession();

            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", user_dto.getEmail()));

            if (!criteria1.list().isEmpty()) {
                response_dto.setContent("User alreasy registered with this email");
            } else {

                //generate verification code
                int code = (int) (Math.random() * 1000000);

                final User user = new User();
                user.setFirst_name(user_dto.getFirst_name());
                user.setLast_name(user_dto.getLast_name());
                user.setEmail(user_dto.getEmail());
                user.setPassword(user_dto.getPassword());
                user.setVerification(String.valueOf(code));

                Mail.sendMail(user.getEmail(), "Verification Code Tulipvase ", "<h1 style=\"color:#4f1e66\">" + user.getVerification() + "<h1>");

                session.save(user);
                session.beginTransaction().commit();

                request.getSession().setAttribute("email", user_dto.getEmail());
                response_dto.setSuccess(true);
                response_dto.setContent("User Register Successfully");
            }

            session.close();

        }
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_dto));
    }

}
