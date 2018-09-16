package WebServer;
/**
 * Created by Tim on 10/12/2017.
 */

import com.sun.deploy.util.SyncFileAccess;
import java.io.*;
import java.net.*;
import java.util.*;
import java.security.cert.CRL;



public class WebServer {
    public static void main(String argv[]) throws Exception {
        //set port number
        int port = 6989;

        //set socket
        ServerSocket please = new ServerSocket(port);

        while (true) {
            Socket connection = please.accept();
            System.out.println("Request Received");

            // Construct an object to process the HTTP request message.
            HttpRequest request = new HttpRequest(connection);

            // Create a new thread to process the request.
            Thread thread = new Thread(request);

            // Start the thread.
            thread.start();

        }
    }
}

 class HttpRequest implements Runnable{
     static String CRLF = "\r\n";
     Socket socket;

     // Constructor
     public HttpRequest(Socket socket) throws Exception
     {
         this.socket = socket;
     }

     // Implement the run() method of the Runnable interface.
     public void run()
     {
         try{
             processRequest();
         }catch(Exception e){
             System.out.println(e);
         }

     }

     private void processRequest() throws Exception
     {
         //Get a reference to the socket's input and output streams.
         InputStream is = socket.getInputStream();
         DataOutputStream os = new DataOutputStream(socket.getOutputStream());

         //Set up input stream filters
         InputStreamReader isr = new InputStreamReader(is);
         BufferedReader br = new BufferedReader(isr);

         //Get the request line of the HTTP request message.
         String requestLine = br.readLine();

         //Display the request line.
         System.out.println();
         System.out.println(requestLine);

         //Get and display the header lines.
         String headerLine = null;
         while((headerLine = br.readLine()).length() != 0){
             System.out.println(headerLine);
         }

         //Extract the filename from the request line
         StringTokenizer tokens = new StringTokenizer(requestLine);
         tokens.nextToken(); //skip over the method, which should be "GET"
         String fileName = tokens.nextToken();

         //prepend a "." so that file request is within the current directory
         fileName = "." + fileName;

         //open the request file
         FileInputStream fis = null;
         boolean fileExists = true;
         try{
             fis=new FileInputStream(fileName);
         }catch(FileNotFoundException e){
             fileExists = false;
         }

         //Construct the response message
         String statusLine = null;
         String contentTypeLine = null;
         String entityBody = null;

         if(fileExists){
             statusLine = "HTTP/1.1 200 OK" +CRLF;
             contentTypeLine = "Content-type: " +
                     contentType(fileName) + CRLF;
             System.out.println(contentTypeLine);
         }else{
             statusLine = "HTTP/1.1 404 File Not Found" +CRLF;
             contentTypeLine = "Content-type: Error" +CRLF;
             entityBody = "<HTML>" +
                     "<HEAD><TITLE>Not Found</TITLE></HEAD>" +
                     "<BODY>Not Found</BODY></HTML>";
         }

         //Send the status line
         os.writeBytes(statusLine);

         //Send the content type line;
         os.writeBytes(contentTypeLine);

         //send a blank line to indicate the end of the header lines.
         os.writeBytes(CRLF);

         //send the entity body
         if(fileExists){
             sendBytes(fis, os);
             fis.close();
         }else{
             os.writeBytes(entityBody);
         }

         //Close streams and socket
         os.flush();
         os.close();
         br.close();
         socket.close();
     }

     private static void sendBytes(FileInputStream fis, OutputStream os)
         throws Exception
     {
         //Construct a 1k buffer to hold bytes on their way to the socket.
         byte [] buffer = new byte[1024];
         int bytes = 0;

         //Copy requested file into the socket's output stream
         while((bytes = fis.read(buffer)) != -1){
             os.write(buffer, 0, bytes);
         }
     }

     private static String contentType(String fileName){
         if(fileName.endsWith(".htm") || fileName.endsWith(".html")){
             return "text/html";
         }
         if(fileName.endsWith(".js")){
             return "text/javascript";
         }
         if(fileName.endsWith(".css")){
             return "text/css";
         }

         if (fileName.endsWith(".png")){
             return "image/png";
         }
         if(fileName.endsWith(".jpeg") || fileName.endsWith(".jpg")){
             return "image/jpeg";
         }
         if(fileName.endsWith(".gif")){
             return "image/gif";
         }
         if(fileName.endsWith(".mp3")){
             return "audio/mpeg";
         }
         return "application/octet-stream";
     }



 }

