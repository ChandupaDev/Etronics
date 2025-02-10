package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dto.Cart_DTO;

import dto.User_DTO;
import entity.Cart;
import entity.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "SignIn", urlPatterns = { "/SignIn" })
public class SignIn extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // response object
        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("success", false);

        // get user data from json to userDTO object
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        User_DTO userDTO = gson.fromJson(request.getReader(), User_DTO.class);

        // validations
        if (userDTO.getEmail().isEmpty()) {
            responseJsonObject.addProperty("errorfrom", "email");
            responseJsonObject.addProperty("content", "Please type email address!");

        } else if (userDTO.getPassword().isEmpty()) {
            responseJsonObject.addProperty("errorfrom", "password");
            responseJsonObject.addProperty("content", "Please type password!");

        } else {
            // validated

            Session session = HibernateUtil.getSessionFactory().openSession();

            // get user from DB
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("email", userDTO.getEmail()));
            criteria.add(Restrictions.eq("password", userDTO.getPassword()));

            if (!criteria.list().isEmpty()) {
                // user found in DB
                User user = (User) criteria.uniqueResult();

                // remove first login status in session
                request.getSession().removeAttribute("loggedFirstTime");

                // check verification code
                if (!user.getVerification().equals("Verified")) {
                    // not verified

                    // set user's email in session
                    request.getSession().setAttribute("email", userDTO.getEmail());
                    responseJsonObject.addProperty("content", "Unverified!");

                } else {
                    // verified
                    userDTO.setFirst_name(user.getFirst_name());
                    userDTO.setLast_name(user.getLast_name());
                    userDTO.setPassword(null);

                    // set user in session
                    request.getSession().setAttribute("user", userDTO);

                    // Transfer session cart to DB cart
                    if (request.getSession().getAttribute("sessionCart") != null) {

                        ArrayList<Cart_DTO> sessionCart = (ArrayList<Cart_DTO>) request.getSession()
                                .getAttribute("sessionCart");

                        Criteria criteria2 = session.createCriteria(Cart.class);
                        criteria2.add(Restrictions.eq("user", user));
                        List<Cart> dbCart = criteria2.list();

                        if (dbCart.isEmpty()) {
                            // DB cart is empty
                            // Add all session cart items into DB cart

                            for (Cart_DTO cart_DTO : sessionCart) {

                                Cart cart = new Cart();
                                cart.setProduct(cart_DTO.getProduct()); // * set user null
                                cart.setQty(cart_DTO.getQty());
                                cart.setUser(user);
                                session.save(cart);
                            }

                        } else {
                            // found items in DB cart

                            for (Cart_DTO cart_DTO : sessionCart) {

                                boolean isFoundInDBCart = false;
                                for (Cart cart : dbCart) {

                                    if (cart_DTO.getProduct().getId() == cart.getProduct().getId()) {
                                        // same item found in session cart & DB cart
                                        isFoundInDBCart = true;

                                        if ((cart_DTO.getQty() + cart.getQty()) <= cart.getProduct().getQty()) {
                                            // quantity available
                                            cart.setQty(cart_DTO.getQty() + cart.getQty());
                                            session.update(cart);

                                        } else {
                                            // quantity not available
                                            // set max available qty (not required)
                                            cart.setQty(cart.getProduct().getQty());
                                            session.update(cart);
                                        }
                                    }

                                }

                                if (!isFoundInDBCart) {
                                    // not found in DB cart
                                    Cart cart = new Cart();
                                    cart.setProduct(cart_DTO.getProduct()); // * set user null
                                    cart.setQty(cart_DTO.getQty());
                                    cart.setUser(user);
                                    session.save(cart);
                                }

                            }

                        }

                        request.getSession().removeAttribute("sessionCart");
                        session.beginTransaction().commit();

                    }
                    // set success response
                    responseJsonObject.addProperty("success", true);
                    responseJsonObject.addProperty("content", "User sign in success!");
                }

            } else {
                responseJsonObject.addProperty("content", "Invalid details!");
            }

            session.close();

        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJsonObject));

    }

}
