package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import dto.User_DTO;
import entity.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "Verification", urlPatterns = {"/Verification"})
public class Verification extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        Response_DTO response_dto = new Response_DTO();
        
        Gson gson = new Gson();
        JsonObject dto = gson.fromJson(request.getReader(), JsonObject.class);
        String verification = dto.get("verification").getAsString();
        
        if (request.getSession().getAttribute("email") != null) {
            
            String email = request.getSession().getAttribute("email").toString();
            
            Session session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", email));
            criteria1.add(Restrictions.eq("verification", verification));
            
            if (!criteria1.list().isEmpty()) {
                
                User user = (User) criteria1.list().get(0);
                user.setVerification("Verified");
                
                session.update(user);
                session.beginTransaction().commit();
                
                request.getSession().removeAttribute("email");
                
                User_DTO user_dto = new User_DTO();
                user_dto.setFirst_name(user.getFirst_name());
                user_dto.setLast_name(user.getLast_name());
                user_dto.setEmail(email);
                request.getSession().setAttribute("user", user_dto);
                
                response_dto.setSuccess(true);
                response_dto.setContent("Verification Success");
                
            } else {
                //inavalid verification code
                response_dto.setContent("Inavlid Verification Code!");
            }
            
        } else {
            response_dto.setContent("Verification Unavailable! Please Sign in again");
            
        }
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_dto));
        System.out.println(gson.toJson(response_dto));
    }
    
}
