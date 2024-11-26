package pe.edu.upeu.boticaamanecer.servicio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.boticaamanecer.dto.ModeloDataAutocomplet;
import pe.edu.upeu.boticaamanecer.modelo.Cliente;
import pe.edu.upeu.boticaamanecer.repositorio.ClienteRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repo;

    private final Logger logger = LoggerFactory.getLogger(ClienteService.class);

    /**
     * Guardar un cliente.
     *
     * @param cliente El cliente a guardar.
     * @return El cliente guardado.
     */
    public Cliente save(Cliente cliente) {
        try {
            return repo.save(cliente);  // Guardar cliente
        } catch (Exception e) {
            logger.error("Error al guardar el cliente: {}", e.getMessage(), e);  // Loguear el error
            throw new RuntimeException("No se pudo guardar el cliente. Verifique los datos.");  // Lanza una excepción personalizada
        }
    }

    /**
     * Listar todos los clientes.
     *
     * @return Lista de clientes.
     */
    public List<Cliente> list() {
        try {
            return repo.findAll();
        } catch (Exception e) {
            logger.error("Error al listar clientes: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudieron recuperar los clientes.");
        }
    }

    /**
     * Actualizar un cliente por su ID.
     *
     * @param cliente Cliente con los nuevos datos.
     * @param dniruc  ID del cliente a actualizar.
     * @return Cliente actualizado o null si no se encontró.
     */
    public Cliente update(Cliente cliente, String dniruc) {
        try {
            Cliente clienteExistente = repo.findById(dniruc).orElse(null);
            if (clienteExistente != null) {
                clienteExistente.setNombres(cliente.getNombres());
                clienteExistente.setRepLegal(cliente.getRepLegal());
                clienteExistente.setTipoDocumento(cliente.getTipoDocumento());
                return repo.save(clienteExistente);
            } else {
                logger.warn("Cliente con ID {} no encontrado para actualizar.", dniruc);
            }
        } catch (Exception e) {
            logger.error("Error al actualizar cliente con ID {}: {}", dniruc, e.getMessage(), e);
        }
        return null;
    }

    /**
     * Eliminar un cliente por su ID.
     *
     * @param dniruc ID del cliente a eliminar.
     */
    public void delete(String dniruc) {
        try {
            if (repo.existsById(dniruc)) {
                repo.deleteById(dniruc);
            } else {
                logger.warn("Cliente con ID {} no encontrado para eliminar.", dniruc);
            }
        } catch (Exception e) {
            logger.error("Error al eliminar cliente con ID {}: {}", dniruc, e.getMessage(), e);
            throw new RuntimeException("No se pudo eliminar el cliente.");
        }
    }

    /**
     * Buscar un cliente por su ID.
     *
     * @param dniruc ID del cliente a buscar.
     * @return Cliente encontrado o null si no existe.
     */
    public Cliente searchById(String dniruc) {
        try {
            return repo.findById(dniruc).orElse(null);
        } catch (Exception e) {
            logger.error("Error al buscar cliente con ID {}: {}", dniruc, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Obtener una lista de clientes para autocompletado.
     *
     * @return Lista de datos de autocompletado.
     */
    public List<ModeloDataAutocomplet> listAutoCompletCliente() {
        List<ModeloDataAutocomplet> listarclientes = new ArrayList<>();
        try {
            for (Cliente cliente : repo.findAll()) {
                ModeloDataAutocomplet data = new ModeloDataAutocomplet();
                data.setIdx(cliente.getDniruc());
                data.setNameDysplay(cliente.getNombres());
                data.setOtherData(cliente.getTipoDocumento());
                listarclientes.add(data);
            }
        } catch (Exception e) {
            logger.error("Error al obtener datos para autocompletado: {}", e.getMessage(), e);
        }
        return listarclientes;
    }
}
