package pe.edu.upeu.boticaamanecer.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.boticaamanecer.modelo.Proveedor;

@Repository
public interface ProveedorRepository  extends JpaRepository<Proveedor, Long> {
}
