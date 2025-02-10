package controller;

import java.nio.file.Files;
import com.google.gson.Gson;

import dto.Response_DTO;
import dto.User_DTO;
import entity.Category;
import entity.Color;
import entity.Model;
import entity.Product;
import entity.Product_Status;
import entity.User;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@MultipartConfig
@WebServlet(name = "ProductListing1", urlPatterns = {"/ProductListing1"})
public class ProductListing1 extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Response_DTO responseDTO = new Response_DTO();
        Gson gson = new Gson();
        String categoryId = request.getParameter("categoryId");
        String modelId = request.getParameter("modelId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String colorId = request.getParameter("colorId");
        String price = request.getParameter("price");
        String quantity = request.getParameter("quantity");

        // Part image1 = request.getPart("image1");
        // Part image2 = request.getPart("image2");
        // Part image3 = request.getPart("image3");
        /*
         * JsonObject jsonRequest = gson.fromJson(request.getReader(),
         * JsonObject.class);
         * 
         * String categoryId = jsonRequest.get("categoryId").getAsString();
         * String modelId = jsonRequest.get("modelId").getAsString();
         * String title = jsonRequest.get("title").getAsString();
         * String description = jsonRequest.get("description").getAsString();
         * String colorId = jsonRequest.get("colorId").getAsString();
         * String price = jsonRequest.get("price").getAsString();
         * String quantity = jsonRequest.get("quantity").getAsString();
         */
        // Get image files (optional for update)
        Collection<Part> parts = request.getParts();
        Map<String, Part> images = new HashMap<>();
        Iterator<Part> partsIterator = parts.iterator();
        int i = 1;

        while (partsIterator.hasNext()) {
            Part param = partsIterator.next();
            String paramName = param.getName();
            if (paramName.startsWith("image")) {
                System.out.println("parameterName: " + param);
                images.put("image" + i + ".png", param);
                i++;
            }
        }

        Session session = HibernateUtil.getSessionFactory().openSession();

        if (!Validation.isInteger(categoryId)) {
            responseDTO.setContent("Invalid Category");

        } else if (!Validation.isInteger(modelId)) {
            responseDTO.setContent("Invalid Model");

        } else if (title.isEmpty()) {
            responseDTO.setContent("Please fill Title");

        } else if (description.isEmpty()) {
            responseDTO.setContent("Please fill Description");

        } else if (!Validation.isInteger(colorId)) {

            responseDTO.setContent("Invalid color");

        } else if (price.isEmpty()) {
            responseDTO.setContent("Please fill Price");

        } else if (!Validation.isDouble(price)) {
            responseDTO.setContent("Invalid price");

        } else if (Double.parseDouble(price) <= 0) {
            responseDTO.setContent("Price must be greater than 0");

        } else if (quantity.isEmpty()) {
            responseDTO.setContent("Invalid Quantity");

        } else if (!Validation.isInteger(quantity)) {
            responseDTO.setContent("Invalid Quantity");

        } else if (Integer.parseInt(quantity) <= 0) {
            responseDTO.setContent("Quantity must be greater than 0");

        } else if (images.size() < 3) {
            responseDTO.setContent("Please upload 3 images");

        } else {

            Category category = (Category) session.get(Category.class, Integer.valueOf(categoryId));

            if (category == null) {
                responseDTO.setContent("Please select a valid Category");

            } else {

                Model model = (Model) session.get(Model.class, Integer.valueOf(modelId));

                if (model == null) {
                    responseDTO.setContent("Please select a valid Model");

                } else {

                    if (model.getCategory().getId() != category.getId()) {
                        responseDTO.setContent("Please select a valid Model");

                    } else {

                        Color color = (Color) session.get(Color.class, Integer.valueOf(colorId));

                        if (color == null) {

                            responseDTO.setContent("Please select a valid color");

                        } else {
                            // to do Insert

                            Product product = new Product();
                            product.setColor(color);
                            product.setDate_time(new Date());
                            product.setDescription(description);
                            product.setModel(model);
                            product.setPrice(Double.parseDouble(price));

                            Product_Status product_Status = (Product_Status) session.load(Product_Status.class, 1);
                            product.setProductStatus(product_Status);
                            product.setQty(Integer.parseInt(quantity));
                            product.setTitle(title);

                            // get user
                            User_DTO userDto = (User_DTO) request.getSession().getAttribute("user");
                            Criteria criteria1 = session.createCriteria(User.class);
                            criteria1.add(Restrictions.eq("email", userDto.getEmail()));
                            User user = (User) criteria1.uniqueResult();
                            product.setUser(user);

                            session.beginTransaction();
                            int pid = (int) session.save(product);
                            session.getTransaction().commit();

                            String applicationPath = request.getServletContext().getRealPath("");
                            String newApplicationPath = applicationPath.replace("build" + File.separator + "web",
                                    "web");

                            File folder = new File(newApplicationPath + "/product-images/" + pid);
                            folder.mkdir();

                            // Update images (if provided)
                            for (Map.Entry<String, Part> entry : images.entrySet()) {
                                String name = entry.getKey();
                                Part image = entry.getValue();

                                if (image != null && image.getSubmittedFileName() != null) {
                                    File file1 = new File(folder, name);
                                    try (InputStream inputStream1 = image.getInputStream()) {
                                        Files.copy(inputStream1, file1.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                    } catch (IOException ex) {
                                        Logger.getLogger(UpdateProduct.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }

                            responseDTO.setSuccess(true);
                            responseDTO.setContent("New Product Addedd");

                        }

                    }

                }

            }

            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(responseDTO));
            session.close();

        }

    }
}
