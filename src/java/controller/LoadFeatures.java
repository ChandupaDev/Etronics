package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Category;
import entity.Color;
import entity.Model;
import entity.Product;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

@WebServlet(name = "LoadFeatures", urlPatterns = { "/LoadFeatures" })
public class LoadFeatures extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Gson gson = new Gson();

        Session session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria1 = session.createCriteria(Category.class);
        criteria1.addOrder(Order.asc("name"));
        List<Category> categoryList = criteria1.list();

        Criteria criteria2 = session.createCriteria(Model.class);
        criteria2.addOrder(Order.asc("name"));
        List<Model> modelList = criteria2.list();

        Criteria criteria3 = session.createCriteria(Color.class);
        criteria3.addOrder(Order.asc("name"));
        List<Color> colorList = criteria3.list();

        Criteria criteria4 = session.createCriteria(Product.class);
        criteria4.addOrder(Order.asc("title"));
        List<Product> productList = criteria4.list();

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("categoryList", gson.toJsonTree(categoryList));
        jsonObject.add("modelList", gson.toJsonTree(modelList));
        jsonObject.add("colorList", gson.toJsonTree(colorList));
        jsonObject.add("productList", gson.toJsonTree(productList));

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(jsonObject));

        System.out.println("\njson object");
        System.out.println(gson.toJson(jsonObject));

        session.close();

    }

}
