package dev.mimgr.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.net.http.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

/**
 *
 * @author dn200
 */
public class ResourceManager {
  private ResourceManager() {
    String os = System.getProperty("os.name").toLowerCase();
    final String APP_NAME = "mimgr";

    if (os.contains("win")) {
      // Windows: Use %APPDATA%\MyApp (Roaming) or %LOCALAPPDATA%\MyApp (Local)
      String appData = System.getenv("APPDATA");  // Roaming AppData
      if (appData == null) {
        appData = System.getenv("LOCALAPPDATA");  // Fallback to Local AppData
      }
      appDataPath = Paths.get(appData, APP_NAME);
    } else if (os.contains("mac")) {
      // macOS: Use ~/Library/Application Support/MyApp
      String userHome = System.getProperty("user.home");
      appDataPath = Paths.get(userHome, "Library", "Application Support", APP_NAME);
    } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
      // Linux/Unix: Use ~/.local/share/MyApp
      String userHome = System.getProperty("user.home");
      appDataPath = Paths.get(userHome, ".local", "share", APP_NAME);
    } else {
      throw new UnsupportedOperationException("Unsupported OS: " + os);
    }

    projectPath = Paths.get("").toAbsolutePath();
    uploadPath = Paths.get("uploads").toAbsolutePath();
    tempPath = Path.of(System.getProperty("java.io.tmpdir")).toAbsolutePath().resolve(APP_NAME);

    createDirIfNotExists(appDataPath.toFile());
    createDirIfNotExists(uploadPath.toFile());
    createDirIfNotExists(tempPath.toFile());
  }

  public static ResourceManager getInstance() {
    return ResourceManagerHolder.INSTANCE;
  }

  public static void createDirIfNotExists(File directory) {
    if (!directory.exists()) {
      directory.mkdirs();
    }
  }

  public Path getAppDataDir() {
    return this.appDataPath;
  }

  public Path getProjectPath() {
    return this.projectPath;
  }

  public Path getUploadPath() {
    return this.uploadPath;
  }

  public Path getTempPath() {
    return this.tempPath;
  }

  public Path downloadTempFile(String urlString) {
    Path tempDir = this.tempPath;
    Path path = downloadFileToPath(urlString, tempDir);
    if (tempFiles == null) {
      tempFiles = new ArrayList<File>();
    }
    tempFiles.add(path.toFile());
    return path;
  }

  public void cleanTempFiles() {
    if (tempFiles == null || tempFiles.isEmpty()) return;
    for (File file : tempFiles) {
      if (file.exists()) {
        file.delete();
      }
    }
    tempFiles = null;
  }

  public static Path downloadFileToPath(String urlString, Path saveDirectory) {
    Path filePath = null;

    try {
      URI uri = new URI(urlString);
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder().uri(uri).build();

      // Send the GET request and handle the response as a byte array
      HttpResponse<InputStream> response = client.send(
        request,
        HttpResponse.BodyHandlers.ofInputStream()
      );

      String fileName = getFileNameFromResponse(response);
      System.out.println("Downloaded filename: " + fileName);
      filePath = saveDirectory.resolve(fileName); // Use the extracted filename

      try (InputStream inputStream = response.body()) {
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e) {
        System.err.println("Error writing to file: " + e.getMessage());
      }

      if (response.statusCode() == 200) {
        System.out.println("File downloaded successfully to " + filePath); } else {
        System.out.println("Failed to download image. HTTP status code: " + response.statusCode());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return filePath;
  }

  public static Path getUniquePath(Path destinationPath) {
    int copyNumber = 1;
    Path newPath = destinationPath;

    // Check if the destination file exists, add a suffix if it does
    while (Files.exists(newPath)) {
      String filename = destinationPath.getFileName().toString();
      String newFilename = filename.replaceFirst("(\\.[^.]+)?$", "_" + copyNumber + "$1"); // Adds "_copy" before the file extension
      newPath = destinationPath.resolveSibling(newFilename);
      copyNumber++;
    }

    return newPath;
  }


  private static String getFileNameFromResponse(HttpResponse<InputStream> response) {
    // Check the Content-Disposition header for the filename
    String disposition = response.headers().firstValue("Content-Disposition").orElse("");
    if (!disposition.isEmpty()) {
      String[] parts = disposition.split(";");
      for (String part : parts) {
        part = part.trim();
        if (part.startsWith("filename=")) {
          String fileName = part.substring(9).replace("\"", ""); // Remove quotes if present
          return fileName;
        }
      }
    }
    // Fallback to extracting filename from URL if no Content-Disposition header is found
    return getFileNameFromUrl(response.request().uri().toString());
  }

  private static String getFileNameFromUrl(String fileUrl) {
    // Extract the filename from the URL
    URI uri = URI.create(fileUrl);
    Path path = Paths.get(uri.getPath());
    return path.getFileName().toString(); // Get the filename
  }

  private static class ResourceManagerHolder {
    private static final ResourceManager INSTANCE = new ResourceManager();
  }

  private ArrayList<File> tempFiles = null;
  private Path appDataPath;
  private Path tempPath;
  private Path uploadPath;
  private Path projectPath;
}
