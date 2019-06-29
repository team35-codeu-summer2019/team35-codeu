package com.google.codeu.servlets;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/blobstore-upload-url")
public class BlobstoreUploadUrlServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    String uploadUrl;
    String requester = request.getParameter("requester");
    if (requester.equals("user-page")) {
      uploadUrl = blobstoreService.createUploadUrl("/form-handler");
    } else {
      uploadUrl = blobstoreService.createUploadUrl("/profile-form-handler");
    }
    response.setContentType("text/html");
    response.getOutputStream().println(uploadUrl);
  }

}
