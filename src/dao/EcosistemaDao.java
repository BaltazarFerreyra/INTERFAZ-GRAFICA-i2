package dao;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import modelo.Ecosistema;

public final class EcosistemaDao {
    private final Path archivo;

    public EcosistemaDao(Path archivo) {
        this.archivo = archivo;
    }

    public Optional<Ecosistema> cargar() throws IOException {
        try {
            return Optional.of(Ecosistema.restaurarEstado(
                    Files.readString(archivo, StandardCharsets.UTF_8)));
        } catch (java.nio.file.NoSuchFileException ex) {
            return Optional.empty();
        } catch (IllegalArgumentException ex) {
            throw new IOException("El archivo guardado contiene datos inválidos.", ex);
        }
    }

    public void guardar(Ecosistema ecosistema) throws IOException {
        Path carpeta = archivo.toAbsolutePath().getParent();
        Path temporal = Files.createTempFile(carpeta, "simulacion-", ".tmp");
        try {
            Files.writeString(temporal, ecosistema.exportarEstado(), StandardCharsets.UTF_8);
            try {
                Files.move(temporal, archivo, StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException ex) {
                Files.move(temporal, archivo, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporal);
        }
    }

    public void borrar() throws IOException {
        Files.deleteIfExists(archivo);
    }
}
