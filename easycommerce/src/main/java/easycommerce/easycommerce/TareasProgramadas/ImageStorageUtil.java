package easycommerce.easycommerce.TareasProgramadas;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import easycommerce.easycommerce.Articulo.DTOs.ArticuloDTOPost;
import io.jsonwebtoken.security.SecurityException;

public class ImageStorageUtil {

    //private static final String BASE_PATH = System.getenv("urlImagenesPath");
    private static final String BASE_PATH_STRING = "D:\\LaConchaDeTuMadre";
    private static final Path BASE_PATH = Paths.get(BASE_PATH_STRING);
    
    public static List<String> saveImages(List<MultipartFile> files, String subdirectory) throws IOException {
        Path folder = Paths.get(BASE_PATH_STRING, subdirectory);
        Files.createDirectories(folder);

        List<String> savedpath = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String originalName = file.getOriginalFilename();
            Path finalPath = folder.resolve(originalName);
            Files.write(finalPath, file.getBytes());
            savedpath.add(BASE_PATH.relativize(finalPath).toString());
        }
        return savedpath;
    }

    public static List<String> deleteImage(List<String> relativePaths) throws IOException{
        List<String> deletedImage = new ArrayList<>();

        for (String relativePath : relativePaths) {
            Path imagePath = BASE_PATH.resolve(relativePath).normalize();

            if(!imagePath.startsWith(BASE_PATH)){
                throw new SecurityException("La ruta especificada se encuentra fuera del directorio");
            }

            if(Files.deleteIfExists(imagePath)){
                deletedImage.add(relativePath);
            }
        }
        return deletedImage;
    }

    public static List<String> deleteImageByName(String filename) throws IOException {
        List<String> deletedImages = new ArrayList<>();

        try (Stream<Path> files = Files.walk(BASE_PATH)) {
            List<Path> matchedFiles = files
                .filter(path -> !Files.isDirectory(path))
                .filter(path -> path.getFileName().toString().equals(filename))
                .collect(Collectors.toList());

            for (Path imagePath : matchedFiles) {
                if (!imagePath.normalize().startsWith(BASE_PATH)) {
                    throw new SecurityException("La ruta especificada se encuentra fuera del directorio permitido");
                }

                if (Files.deleteIfExists(imagePath)) {
                    deletedImages.add(BASE_PATH.relativize(imagePath).toString());
                }
            }
        }

        return deletedImages;
    }

    public static void saveArticulosJson(List<ArticuloDTOPost> articulos, String subdirectory) throws IOException {
        Path folder = Paths.get(BASE_PATH_STRING, subdirectory);
        Files.createDirectories(folder);

        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int nextIndex = getNextIndex(folder, fecha);
        String filename = String.format("%s-%03d.json", fecha, nextIndex);
        Path finalPath = folder.resolve(filename);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(finalPath.toFile(), articulos);
    }

    private static int getNextIndex(Path folder, String fecha) throws IOException {
    try (Stream<Path> files = Files.list(folder)) {
        return files
            .map(Path::getFileName)
            .map(Path::toString)
            .filter(name -> name.matches(fecha + "-\\d{3}\\.json"))
            .map(name -> Integer.parseInt(name.substring(fecha.length() + 1, fecha.length() + 4)))
            .max(Integer::compareTo)
            .orElse(0) + 1;
        }
    }
}
