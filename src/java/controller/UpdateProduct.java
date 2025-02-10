package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.*;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@MultipartConfig
@WebServlet(name = "UpdateProduct", urlPatterns = { "/UpdateProduct" })
public class UpdateProduct extends HttpServlet {

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Response_DTO responseDTO = new Response_DTO();
        Gson gson = new Gson();
        JsonObject jsonRequest = gson.fromJson(request.getReader(), JsonObject.class);

        // Get parameters
        String productId = jsonRequest.get("productId").getAsString();
        String categoryId = jsonRequest.get("categoryId").getAsString();
        String modelId = jsonRequest.get("modelId").getAsString();
        String title = jsonRequest.get("title").getAsString();
        String description = jsonRequest.get("description").getAsString();
        String colorId = jsonRequest.get("colorId").getAsString();
        String price = jsonRequest.get("price").getAsString();
        String quantity = jsonRequest.get("quantity").getAsString();

        // Get image files (optional for update)
        Enumeration<String> parameterNames = request.getParameterNames();
        Map<String, Part> images = new HashMap<>();
        while (parameterNames.hasMoreElements()) {
            String imageName = parameterNames.nextElement();
            if (imageName.startsWith("image")) {
                images.put(imageName, request.getPart(imageName));
            }
        }

        // Start a Hibernate session
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            // Validate inputs
            if (!Validation.isInteger(productId)) {
                responseDTO.setContent("Invalid Product ID");
            } else if (!Validation.isInteger(categoryId)) {
                responseDTO.setContent("Invalid Category");
            } else if (!Validation.isInteger(modelId)) {
                responseDTO.setContent("Invalid Model");
            } else if (title.isEmpty()) {
                responseDTO.setContent("Please fill Title");
            } else if (description.isEmpty()) {
                responseDTO.setContent("Please fill Description");
            } else if (!Validation.isInteger(colorId)) {
                responseDTO.setContent("Invalid Color");
            } else if (price.isEmpty() || !Validation.isDouble(price) || Double.parseDouble(price) <= 0) {
                responseDTO.setContent("Invalid Price");
            } else if (quantity.isEmpty() || !Validation.isInteger(quantity) || Integer.parseInt(quantity) <= 0) {
                responseDTO.setContent("Invalid Quantity");
            } else {
                // Fetch product from database
                Product product = (Product) session.get(Product.class, Integer.valueOf(productId));
                System.out.println("product: " + product);
                if (product == null) {
                    responseDTO.setContent("Product not found");
                } else {
                    // Fetch related entities
                    Category category = (Category) session.get(Category.class, Integer.valueOf(categoryId));
                    Model model = (Model) session.get(Model.class, Integer.valueOf(modelId));
                    Color color = (Color) session.get(Color.class, Integer.valueOf(colorId));

                    if (category == null) {
                        responseDTO.setContent("Invalid Category");
                    } else if (model == null || model.getCategory().getId() != category.getId()) {
                        responseDTO.setContent("Invalid Model for the selected Category");
                    } else if (color == null) {
                        responseDTO.setContent("Invalid Color");
                    } else {
                        // Update product details
                        session.beginTransaction();
                        product.setModel(model);
                        product.setColor(color);
                        product.setTitle(title);
                        product.setDescription(description);
                        product.setPrice(Double.parseDouble(price));
                        product.setQty(Integer.parseInt(quantity));
                        session.update(product);
                        session.getTransaction().commit();

                        // Update product images (if provided)
                        updateProductImages(request, images, product.getId());

                        responseDTO.setSuccess(true);
                        responseDTO.setContent("Product updated successfully");
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            session.getTransaction().rollback();
            responseDTO.setContent("Error: " + e.getMessage());
        } finally {
            session.close();
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseDTO));
    }

    private void updateProductImages(HttpServletRequest request, Map<String, Part> images, int productId)
            throws IOException {
        String applicationPath = request.getServletContext().getRealPath("");
        String imagesPath = applicationPath.replace("build" + File.separator + "web", "web") + "/product-images/"
                + productId;

        File imagesFolder = new File(imagesPath);
        if (!imagesFolder.exists()) {
            imagesFolder.mkdir();
        }

        // Update images (if provided)
        for (Map.Entry<String, Part> entry : images.entrySet()) {
            String name = entry.getKey();
            Part image = entry.getValue();

            if (image != null && image.getSubmittedFileName() != null) {
                File file1 = new File(imagesFolder, name);
                try (InputStream inputStream1 = image.getInputStream()) {
                    Files.copy(inputStream1, file1.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    Logger.getLogger(UpdateProduct.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
