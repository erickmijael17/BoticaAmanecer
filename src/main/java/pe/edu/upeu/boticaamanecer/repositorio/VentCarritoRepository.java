package pe.edu.upeu.boticaamanecer.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.boticaamanecer.modelo.VentCarrito;

import java.util.List;

@Repository
public interface VentCarritoRepository  extends JpaRepository<VentCarrito, Long> {
    @Query(value = "SELECT c.* FROM vent_carrito c WHERE c.dniruc=:dniruc", nativeQuery = true)
    List<VentCarrito> listaCarritoCliente(@Param("dniruc") String dniruc);

    void deleteByDniruc(String dniruc);

}