package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Category;
import entity.Color;
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

@WebServlet(name = "LoadData", urlPatterns = {"/LoadData"})
public class LoadData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", false);

        Gson gson = new Gson();

        Session session = HibernateUtil.getSessionFactory().openSession();

        //main code
        //get category list from db
        Criteria criteria1 = session.createCriteria(Category.class);
        List<Category> categoryList = criteria1.list();
        jsonObject.add("categoryList", gson.toJsonTree(categoryList));

        //get color list from db
        Criteria criteria3 = session.createCriteria(Color.class);
        List<Color> colorList = criteria3.list();
        jsonObject.add("colorList", gson.toJsonTree(colorList));

        //get product list from db
        Criteria criteria5 = session.createCriteria(Product.class);

        //get latest product
        criteria5.addOrder(Order.desc("id"));
        jsonObject.addProperty("allProductCount", criteria5.list().size());

        //set product range
        criteria5.setFirstResult(0);
        criteria5.setMaxResults(3);

        List<Product> productList = criteria5.list();

        //remove users in product
        for (Product product : productList) {
            product.setUser(null);
        }

        jsonObject.add("productList", gson.toJsonTree(productList));
        jsonObject.addProperty("success", true);

        //main code
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(jsonObject));

    }

}
